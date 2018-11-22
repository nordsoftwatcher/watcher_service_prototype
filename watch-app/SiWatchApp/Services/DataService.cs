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

        private readonly Settings _settings;
        private readonly JsonSerializerSettings _jsonSerializerSettings;

        public DataService(Settings settings)
        {
            _settings = settings;
            _jsonSerializerSettings = new JsonSerializerSettings {
                    ContractResolver = new CamelCasePropertyNamesContractResolver(),
                    Converters = { new StringEnumConverter() }
            };
        }

        public async Task Send(DataPacket packet)
        {
            var httpClient = new HttpClient { BaseAddress = new Uri(_settings.ApiUrl) };
            try {
                var json = JsonConvert.SerializeObject(packet, Formatting.None, _jsonSerializerSettings);
                HttpContent content = new StringContent(json);
                content.Headers.ContentType = MediaTypeHeaderValue.Parse("application/json");

                var response = await httpClient.PutAsync("data", content);
                if (!response.IsSuccessStatusCode)
                {
                    LOGGER.Warn("Unexpected HTTP status", response.StatusCode);
                    throw new ApplicationException($"Unexpected HTTP status ${response.StatusCode} while sending data");
                }
            }
            catch (Exception ex)
            {
                LOGGER.Error("Failed sending data:", ex);
                throw;
            }
        }
    }
}
