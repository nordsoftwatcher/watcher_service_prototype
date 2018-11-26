using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Reactive.Subjects;
using System.Threading.Tasks;
using SiWatchApp.Logging;
using SiWatchApp.Utils;
using Tizen.Applications.Exceptions;
using Tizen.Security;

namespace SiWatchApp.Services
{
    public class PermissionManager
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(PermissionManager));

        public Task Demand(params string[] privileges)
        {
            if (privileges == null || privileges.Length == 0) {
                return Task.CompletedTask;
            }

            LOGGER.Debug($"Demand permissions [{String.Join(",", privileges)}]");

            var needToAsk = new List<string>();
            var denied = new List<string>();

            foreach (var privilege in privileges) {
                LOGGER.Debug($"Checking permission {privilege}");
                CheckResult result = PrivacyPrivilegeManager.CheckPermission(privilege);
                if (result == CheckResult.Ask) {
                    needToAsk.Add(privilege);
                    LOGGER.Debug($"Permission {privilege} should be asked");
                }
                else if (result == CheckResult.Deny) {
                    denied.Add(privilege);
                    LOGGER.Debug($"Permission {privilege} is denied");
                }
                else {
                    LOGGER.Debug($"Permission {privilege} is allowed");
                }
            }

            if (denied.Count > 0) {
                return Task.FromException(new PermissionDeniedException($"Permissions [{String.Join(",", denied)}] denied"));
            }

            if (needToAsk.Count == 0) {
                return Task.CompletedTask;
            }

            TaskCompletionSource<bool> tcs = new TaskCompletionSource<bool>();
            int count = 0;
            needToAsk.ForEach(privilegeToAsk =>
                                      new PermissionRequest(
                                              privilegeToAsk,
                                              (p, allowed) => {
                                                  if (!allowed) denied.Add(p);
                                                  if (++count == needToAsk.Count) {
                                                      if (denied.Count > 0) {
                                                          tcs.SetException(
                                                                  new PermissionDeniedException(
                                                                          $"Permissions [{String.Join(",", denied)}] denied"));
                                                      }
                                                      else {
                                                          tcs.SetResult(true);
                                                      }
                                                  }
                                              }));

            return tcs.Task;
        }

        private struct PermissionRequest
        {
            private readonly Action<string, bool> _callback;

            public PermissionRequest(string privilege, Action<string, bool> callback)
            {
                _callback = callback;

                PrivacyPrivilegeManager.ResponseContext context;
                PrivacyPrivilegeManager.GetResponseContext(privilege).TryGetTarget(out context);
                if (context != null)
                {
                    context.ResponseFetched += ResponseHandler;
                }
                else
                {
                    throw new ApplicationException($"Failed requesting permission {privilege}");
                }
               
                PrivacyPrivilegeManager.RequestPermission(privilege);
            }

            private void ResponseHandler(object sender, RequestResponseEventArgs args)
            {
                PrivacyPrivilegeManager.ResponseContext context;
                PrivacyPrivilegeManager.GetResponseContext(args.privilege).TryGetTarget(out context);
                if (context != null)
                {
                    context.ResponseFetched -= ResponseHandler;
                }

                if (args.cause == CallCause.Answer)
                {
                    switch (args.result)
                    {
                        case RequestResult.AllowForever:
                            LOGGER.Debug($"User allowed usage of privilege {args.privilege} definitely");
                            _callback(args.privilege, true);
                            return;
                        case RequestResult.DenyForever:
                            LOGGER.Debug($"User denied usage of privilege {args.privilege} definitely");
                            break;
                        case RequestResult.DenyOnce:
                            LOGGER.Debug($"User denied usage of privilege {args.privilege} this time");
                            break;
                    }
                }
                else
                {
                    LOGGER.Warn($"Error occured during requesting permission for {args.privilege}");
                }
                _callback(args.privilege, false);
            }
        }
    }
}
