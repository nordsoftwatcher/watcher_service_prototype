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
    public class BufferingSyncProxy : ISyncProxy
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(BufferingSyncProxy));

        private readonly JsonSerializerSettings _jsonSerializerSettings;
        private readonly SyncClient _syncClient;
        private readonly IJsonStorage _storage;
        private readonly Settings _settings;

        public BufferingSyncProxy(IJsonStorage storage, Settings settings)
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
        
        public async Task<SyncPacket> Sync(SyncPacket packet)
        {
            packet.DeviceId = _settings.DeviceId;
            var packetJson = JsonConvert.SerializeObject(packet, Formatting.None, _jsonSerializerSettings);

            await _storage.Append(packetJson);
            
            string jsonOut = await _storage.Get();
            string jsonIn;
            try {
                jsonIn = await _syncClient.Send(jsonOut);
            }
            catch (Exception ex) {
                LOGGER.Error("Failed sending sync data:", ex);
                Synced?.Invoke(this, SyncResult.Delayed);
                return null;
            }

            await _storage.Clear();

            Synced?.Invoke(this, SyncResult.Sent);

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

        public event EventHandler<SyncResult> Synced;
    }

    public enum SyncResult
    {
        Sent,
        Delayed
    }
}
