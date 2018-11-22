using System;
using System.Collections.Generic;
using System.Collections.Immutable;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using Newtonsoft.Json.Serialization;
using SiWatchApp.Configuration;
using SiWatchApp.Models;
using SiWatchApp.Monitors;
using SiWatchApp.Utils;
using Xamarin.Forms.Internals;

namespace SiWatchApp.Services
{
    public class DataService
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(DataService));

        private static readonly MediaTypeHeaderValue JSON = MediaTypeHeaderValue.Parse("application/json");

        private readonly Settings _settings;
        private readonly JsonSerializerSettings _jsonSerializerSettings;

        public DataService(Settings settings)
        {
            _settings = settings;
            _jsonSerializerSettings = new JsonSerializerSettings {
                    ContractResolver = new CamelCasePropertyNamesContractResolver(),
                    Converters = { new StringEnumConverter() },
                    NullValueHandling = NullValueHandling.Ignore
            };
        }

        private async Task<DataPacket> TrySend(DataPacket packet)
        {
            using (var httpClient = new HttpClient { BaseAddress = new Uri(_settings.ApiUrl) }) {
                HttpResponseMessage response;
                try {
                    var jsonOut = JsonConvert.SerializeObject(packet, Formatting.None, _jsonSerializerSettings);
                    HttpContent content = new StringContent(jsonOut);
                    content.Headers.ContentType = JSON;
                    response = await httpClient.PutAsync("sync", content);
                }
                catch (Exception ex) {
                    LOGGER.Error("Failed sending data:", ex);
                    throw;
                }

                if (response.IsSuccessStatusCode) {
                    if (response.Content?.Headers?.ContentType != null && response.Content.Headers.ContentType.Equals(JSON)) {
                        try {
                            string jsonIn = await response.Content.ReadAsStringAsync();
                            if (!String.IsNullOrEmpty(jsonIn)) {
                                var reply = JsonConvert.DeserializeObject<DataPacket>(jsonIn, _jsonSerializerSettings);
                                return reply;
                            }
                        }
                        catch (Exception ex) {
                            LOGGER.Warn("Failed reading incoming data:", ex);
                        }
                    }
                }
                else {
                    LOGGER.Warn($"Unexpected HTTP status ${(int)response.StatusCode}");
                    //throw new ApplicationException($"Unexpected HTTP status ${(int)response.StatusCode}");
                }
                return null;
            }
        }
        
        public async Task<DataPacket> Send(DataPacket packet)
        {
            packet.DeviceId = _settings.DeviceId;

            var attempts = _settings.SendRetryCount + 1;
            Exception lastException = null;

            while (attempts-- > 0) {
                try {
                    return await TrySend(packet);
                }
                catch (HttpRequestException ex) {
                    lastException = ex;
                    LOGGER.Warn("Failed sending packet. Will retry after delay");
                    await Task.Delay(_settings.SendRetryDelay);
                }
                catch (Exception ex) {
                    lastException = ex;
                    break;
                }
            }
            if (lastException != null) {
                throw lastException;
            }

            return null;
        }
    }
}
