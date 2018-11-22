using System.Threading.Tasks;
using SiWatchApp.Configuration;
using Tizen.Applications;

namespace SiWatchApp.Services
{
    public class SettingsService
    {
        private readonly Settings _settings;

        public SettingsService()
        {
            var metadata = Application.Current.ApplicationInfo.Metadata;
            _settings = new Settings {
                    DeviceId = metadata["deviceId"],
                    ApiUrl = metadata["apiUrl"]
            };
        }

        public async Task<Settings> GetSettings()
        {
            return _settings;
        }
    }
}
