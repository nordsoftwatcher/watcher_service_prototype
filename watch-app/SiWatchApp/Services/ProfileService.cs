using System;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using Newtonsoft.Json;
using SiWatchApp.Configuration;
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
            var httpClient = new HttpClient { BaseAddress = new Uri(_settings.ApiUrl) };

            HttpStatusCode statusCode;
            try {
                var response = await httpClient.GetAsync("profile?deviceId=" + _settings.DeviceId);
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

            LOGGER.Warn("Unexpected HTTP status", statusCode);
            throw new ApplicationException($"Unexpected HTTP status {statusCode} while getting profile");
        }
    }
}
