using System;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using Newtonsoft.Json.Serialization;
using SiWatchApp.Configuration;
using SiWatchApp.Logging;
using SiWatchApp.Models;

namespace SiWatchApp.Services
{
    public class SyncClient
    {
        private const string APPLICATION_JSON = "application/json";

        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(SyncClient));
        
        private readonly Settings _settings;

        public SyncClient(Settings settings)
        {
            _settings = settings;
        }

        private async Task<string> TrySend(HttpClient httpClient, string jsonOut)
        {
            HttpResponseMessage response;
            try {
                //LOGGER.Debug("Sending", jsonOut);
                HttpContent content = new StringContent(jsonOut, Encoding.UTF8, APPLICATION_JSON);
                response = await httpClient.PutAsync("sync", content);
            }
            catch (Exception ex) {
                LOGGER.Error("Failed sending data:", ex);
                throw;
            }

            if (response.IsSuccessStatusCode) {
                if (response.Content?.Headers?.ContentType != null && response.Content.Headers.ContentType.MediaType.Equals(APPLICATION_JSON)) {
                    try {
                        string jsonIn = await response.Content.ReadAsStringAsync();
                        //LOGGER.Debug("Got", jsonIn);
                        return jsonIn;
                    }
                    catch (Exception ex) {
                        LOGGER.Warn("Failed reading incoming data:", ex);
                    }
                }
            }
            else {
                string message = $"Unexpected HTTP status {(int) response.StatusCode} ({response.StatusCode})";
                LOGGER.Error(message);
                throw new ApplicationException(message);
            }

            return null;
        }

        public async Task<string> Send(string json)
        {
            Exception lastException = null;
            int attempts = _settings.SyncSendRetryCount;
            using (var httpClient = new HttpClient { BaseAddress = new Uri(_settings.ApiUrl), Timeout = _settings.SyncSendHttpTimeout }) {
                while (attempts-- >= 0) {
                    try {
                        return await TrySend(httpClient, json);
                    }
                    catch (HttpRequestException ex) {
                        lastException = ex;
                        if (attempts < 0) {
                            LOGGER.Warn("Failed sending packet");
                        } else {
                            LOGGER.Warn($"Failed sending packet. Will retry after {(int)_settings.SyncSendRetryDelay.TotalMilliseconds}ms");
                        }
                        await Task.Delay(_settings.SyncSendRetryDelay);
                    }
                    catch (Exception ex) {
                        lastException = ex;
                        break;
                    }
                }
            }

            if (lastException != null) {
                throw lastException;
            }

            return null;
        }
    }
}
