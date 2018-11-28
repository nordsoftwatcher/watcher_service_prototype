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
    public class SyncManager
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(SyncManager));

        private readonly JsonSerializerSettings _jsonSerializerSettings;
        private readonly SyncClient _syncClient;
        private readonly IJsonStorage _storage;
        private readonly Settings _settings;

        public SyncManager(IJsonStorage storage, Settings settings)
        {
            _storage = storage;
            _settings = settings;
            _jsonSerializerSettings = new JsonSerializerSettings {
                    Formatting = Formatting.None,
                    ContractResolver = new CamelCasePropertyNamesContractResolver(),
                    Converters = { new StringEnumConverter() },
                    NullValueHandling = NullValueHandling.Ignore
            };
            _syncClient = new SyncClient(settings);
        }
        
        public async Task<SyncPacket> Send(SyncPacket packet)
        {
            packet.DeviceId = _settings.DeviceId;
            var packetJson = JsonConvert.SerializeObject(packet, Formatting.None, _jsonSerializerSettings);

            await _storage.Append(packetJson);

            string jsonOut = await _storage.Get();
            string jsonIn;
            try {
                jsonIn = await _syncClient.Send(jsonOut);
                await _storage.Clear();
            }
            catch (Exception ex) {
                LOGGER.Error("Failed sending sync data:", ex);
                return null;
            }

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
