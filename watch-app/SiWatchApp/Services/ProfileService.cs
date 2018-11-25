using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using Newtonsoft.Json;
using SiWatchApp.Configuration;
using SiWatchApp.Logging;
using SiWatchApp.Monitors;
using SiWatchApp.Utils;

namespace SiWatchApp.Services
{
    public class ProfileService
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(ProfileService));

        private readonly Settings _settings;

        public ProfileService(Settings settings)
        {
            _settings = settings;
        }

        public async Task<Profile> GetProfile()
        {
            using (var httpClient = new HttpClient { BaseAddress = new Uri(_settings.ApiUrl) }) {
                var path = "profile?deviceId=" + _settings.DeviceId;
                LOGGER.Debug("Getting profile from", path);
                HttpStatusCode statusCode;
                try {
                    var response = await httpClient.GetAsync(path);
                    statusCode = response.StatusCode;
                    if (response.IsSuccessStatusCode) {
                        var content = response.Content;
                        using (content) {
                            var json = await content.ReadAsStringAsync();
                            var profile = JsonConvert.DeserializeObject<Profile>(json);
                            LOGGER.Debug("Got profile:", profile);
                            return profile;
                        }
                    }
                }
                catch (Exception ex) {
                    LOGGER.Error("Failed getting profile:", ex);
                    throw;
                }

                LOGGER.Error("Unexpected HTTP status", statusCode);
                throw new ApplicationException($"Unexpected HTTP status {statusCode} while getting profile");
            }
        }

        public async Task CheckProfile(Profile profile, PermissionManager permissionManager)
        {
            if (profile == null || profile.Monitoring == null || profile.Monitoring.Count == 0) {
                return;
            }

            ICollection<IMonitor> monitors = null;
            try {
                monitors = profile.Monitoring
                                  .SelectMany(m => m.Monitors)
                                  .Select(m => m.Type)
                                  .Distinct()
                                  .Select(MonitorBuilder.CreateMonitor)
                                  .Where(m => m != null)
                                  .ToList();

                string[] privileges = monitors
                                      .Select(m => m.Privileges)
                                      .Where(p => p != null)
                                      .SelectMany(p => p)
                                      .Distinct()
                                      .ToArray();

                await permissionManager.Demand(privileges);

                List<IMonitor> unsupported = monitors.Where(m => !m.IsSupported).ToList();
                if (unsupported.Count > 0) {
                    throw new ApplicationException(
                            $"Required monitors [{String.Join(",", unsupported)}] not supported");
                }
            }
            finally {
                if (monitors != null) {
                    foreach (var monitor in monitors) {
                        monitor.Dispose();
                    }
                }
            }
        }
    }
}
