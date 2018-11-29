using System;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using Newtonsoft.Json.Serialization;
using SiWatchApp.Configuration;
using SiWatchApp.Logging;
using SiWatchApp.Models;

namespace SiWatchApp.Services
{
    public class DirectSyncProxy : ISyncProxy
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(DirectSyncProxy));

        private readonly JsonSerializerSettings _jsonSerializerSettings;
        private readonly SyncClient _syncClient;
        private readonly Settings _settings;

        public DirectSyncProxy(Settings settings)
        {
            _settings = settings;
            _jsonSerializerSettings = new JsonSerializerSettings {
                    Formatting = Formatting.None,
                    ContractResolver = new CamelCasePropertyNamesContractResolver(),
                    Converters = { new StringEnumConverter() },
                    NullValueHandling = NullValueHandling.Ignore
            };
            _syncClient = new SyncClient(settings);
        }

        public async Task<SyncPacket> Sync(SyncPacket packet)
        {
            packet.DeviceId = _settings.DeviceId;
            var jsonOut = "["+JsonConvert.SerializeObject(packet, Formatting.None, _jsonSerializerSettings)+"]";

            string jsonIn = await _syncClient.Send(jsonOut);
            
            if (!string.IsNullOrEmpty(jsonIn)) {
                try {
                    return JsonConvert.DeserializeObject<SyncPacket>(jsonIn, _jsonSerializerSettings);
                }
                catch (Exception ex) {
                    LOGGER.Warn("Failed parsing sync response:", ex);
                }
            }
            return null;
        }
    }
}
