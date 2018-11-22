using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using SiWatchApp.Utils;
using Tizen.Applications.Exceptions;
using Tizen.Security;

namespace SiWatchApp.Services
{
    public class PermissionManager
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(PermissionManager));

        public PermissionManager() { }

        public Task Demand(ICollection<string> privileges)
        {
            TaskCompletionSource<bool> tcs = new TaskCompletionSource<bool>();

            if (privileges == null || privileges.Count == 0) {
                tcs.SetResult(true);
                return tcs.Task;
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
                tcs.SetException(new PermissionDeniedException($"Permissions [{String.Join(",", denied)}] denied"));
                return tcs.Task;
            }

            IEnumerator<string> privilegesToAsk = needToAsk.GetEnumerator();
            if (privilegesToAsk.MoveNext()) {
                void AskNextPermission()
                {
                    var privilege = privilegesToAsk.Current;
                    PrivacyPrivilegeManager.GetResponseContext(privilege).TryGetTarget(out var context);
                    if (context != null) {
                        context.ResponseFetched += ResponseHandler;
                    }
                    else {
                        tcs.SetException(new ApplicationException($"Failed requesting permission {privilege}"));
                        return;
                    }
                    PrivacyPrivilegeManager.RequestPermission(privilege);
                }

                void ResponseHandler(object sender, RequestResponseEventArgs args)
                {
                    if (args.cause == CallCause.Answer) {
                        switch (args.result) {
                            case RequestResult.AllowForever:
                                LOGGER.Debug($"User allowed usage of privilege {args.privilege} definitely");
                                break;
                            case RequestResult.DenyForever:
                                LOGGER.Debug($"User denied usage of privilege {args.privilege} definitely");
                                denied.Add(args.privilege);
                                break;
                            case RequestResult.DenyOnce:
                                LOGGER.Debug($"User denied usage of privilege {args.privilege} this time");
                                denied.Add(args.privilege);
                                break;
                        }
                    }
                    else {
                        LOGGER.Warn($"Error occured during requesting permission for {args.privilege}");
                        denied.Add(args.privilege);
                    }

                    PrivacyPrivilegeManager.GetResponseContext(args.privilege).TryGetTarget(out var context);
                    if (context != null) {
                        context.ResponseFetched -= ResponseHandler;
                    }

                    if (privilegesToAsk.MoveNext()) {
                        AskNextPermission();
                    }
                    else {
                        privilegesToAsk.Dispose();
                        if (denied.Count > 0) {
                            tcs.SetException(new PermissionDeniedException($"Permissions [{String.Join(",", denied)}] denied"));
                        }
                        else {
                            tcs.SetResult(true);
                        }
                    }
                }

                AskNextPermission();
            }
            else {
                tcs.SetResult(true);
            }
            return tcs.Task;
        }
    }
}
