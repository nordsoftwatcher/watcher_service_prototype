using System;
using System.ComponentModel.DataAnnotations;
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
                    ApiUrl = metadata["apiUrl"],
                    FeedbackSync = Boolean.Parse(metadata["feedbackSync"]),
                    SyncSendRetryCount = 0,
                    SyncSendRetryDelay = TimeSpan.FromSeconds(3),
                    DefaultSyncPacketSize = 10,
                    SyncSendHttpTimeout = TimeSpan.FromSeconds(15)
            };
        }

        public Task<Settings> GetSettings()
        {
            return Task.FromResult(_settings);
        }
    }
}
