using System;
using System.Collections.Generic;
using System.Reactive.Concurrency;
using System.Reactive.Linq;
using System.Threading.Tasks;
using SiWatchApp.Configuration;
using SiWatchApp.Models;
using SiWatchApp.Monitors;
using SiWatchApp.Queue;
using SiWatchApp.Utils;

namespace SiWatchApp.Services
{
    public class SyncService
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(SyncService));

        private readonly object _sync = new object();

        private readonly IPriorityQueue _queue;
        private readonly MonitoringPolicyService _policyService;
        private readonly DataService _dataService;
        private readonly Settings _settings;
        private IDisposable _subscription = null;

        public SyncService(
                IPriorityQueue queue,
                MonitoringPolicyService policyService,
                DataService dataService,
                Settings settings)
        {
            _queue = queue;
            _dataService = dataService;
            _settings = settings;

            _policyService = policyService;
            _policyService.MonitoringPolicyChanged += HandleMonitoringPolicyChanged;
        }

        private void HandleMonitoringPolicyChanged(object sender, MonitoringPolicy mp)
        {
            Reconfigure(mp);
        }
        
        private void Dismiss()
        {
            if (_subscription != null) {
                _subscription.Dispose();
                _subscription = null;
            }
        }
        
        private async Task<DataPacket> PrepareDataPacket(int size)
        {
            List<MonitoringRecord> monitoringRecords = new List<MonitoringRecord>();
            List<EventRecord> eventRecords = new List<EventRecord>();

            var count = size;
            while (count-- > 0) {
                var record = await _queue.Get();
                if (record != null) {
                    if (record is MonitoringRecord monitoringRecord) {
                        monitoringRecords.Add(monitoringRecord);
                    }
                    else if (record is EventRecord eventRecord) {
                        eventRecords.Add(eventRecord);
                    }
                }
                else {
                    break;
                }
            }

            if (monitoringRecords.Count == 0 && eventRecords.Count == 0)
                return null;

            return new DataPacket() {
                    DeviceId = _settings.DeviceId,
                    Monitoring = monitoringRecords.Count == 0 ? null : monitoringRecords.GroupToDictionary(r => r.MonitorType, r => r.Value),
                    Events = eventRecords.Count == 0 ? null : eventRecords
            };
        }

        private async void Flush(DataPacket packet)
        {
            try {
                await _dataService.Send(packet);
                LOGGER.Debug($"Packet sent");
            }
            catch (Exception ex) {
                throw;
            }
        }

        private void Reconfigure(MonitoringPolicy mp)
        {
            lock (_sync)
            {
                Dismiss();

                _subscription = Observable.Interval(TimeSpan.FromSeconds(mp.FlushInterval), TaskPoolScheduler.Default)
                                    .Select(_ => PrepareDataPacket(mp.PacketSize))
                                    .Subscribe(async t => {
                                                   var packet = await t;
                                                   if(packet != null) Flush(packet);
                                               });
            }
        }

        public void Start()
        {
            Reconfigure(_policyService.CurrentMonitoringPolicy);
            LOGGER.Debug("Started");
        }

        public void Pause()
        {
            throw new NotImplementedException();
        }

        public void Stop()
        {
            lock (_sync)
            {
                Dismiss();
            }
            LOGGER.Debug("Stopped");
        }

    }
}
