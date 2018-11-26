using System;
using System.Collections.Generic;
using System.Linq;
using System.Reactive.Concurrency;
using System.Reactive.Linq;
using System.Threading.Tasks;
using SiWatchApp.Buffer;
using SiWatchApp.Configuration;
using SiWatchApp.Logging;
using SiWatchApp.Models;
using SiWatchApp.Utils;

namespace SiWatchApp.Services
{
    public class SyncService
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(SyncService));

        private readonly object _sync = new object();

        private readonly MonitoringPolicyService _policyService;
        private readonly IBuffer<Record> _buffer;
        private readonly IObserver<EventRecord> _incomingEventsObserver;
        private readonly SyncClient _syncClient;
        private readonly Settings _settings;
        private IDisposable _subscription = null;

        public SyncService(MonitoringPolicyService policyService, IBuffer<Record> buffer, Settings settings, IObserver<EventRecord> incomingEventsObserver = null)
        {
            _buffer = buffer;
            _incomingEventsObserver = incomingEventsObserver;
            _settings = settings;

            _syncClient = new SyncClient(_settings);
            _policyService = policyService;
        }

        private void HandleMonitoringPolicyChanged(object sender, MonitoringPolicy mp)
        {
            Reconfigure(mp);
        }
        
        private void Unsubscribe()
        {
            if (_subscription != null) {
                _subscription.Dispose();
                _subscription = null;
            }
        }
        
        private SyncPacket PreparePacket(IBlock<Record> recordBlock)
        {
            var packet = new SyncPacket();
            if (recordBlock == null || recordBlock.IsEmpty) {
                return packet;
            }

            var records = recordBlock.Items;
            packet.Monitors = records.OfType<MonitorRecord>().ToList();
            packet.Events = records.OfType<EventRecord>().ToList();

            return packet;
        }

        private void ProcessIncomingPacket(SyncPacket incoming)
        {
            if (incoming?.Events != null && _incomingEventsObserver != null) {
                foreach (var incomingEvent in incoming.Events) {
                    _incomingEventsObserver.OnNext(incomingEvent);
                }
            }
        }

        private async Task Sync(int packetSize)
        {
            LOGGER.Debug("Begin sync");

            IBlock<Record> recordBlock;
            LOGGER.Debug($"Fetching {packetSize} records from buffer");
            try {
                recordBlock = _buffer.Get(packetSize);
            }
            catch (Exception ex) {
                LOGGER.Error("Failed fetching next records:", ex);
                return;
            }

            try {
                var packet = PreparePacket(recordBlock);
                var incoming = await _syncClient.Send(packet);
                try {
                    recordBlock?.Discard();
                }
                catch (Exception ex) {
                    LOGGER.Error("Record block (Discard) error:", ex);
                    throw;
                }
                recordBlock = null;

                ProcessIncomingPacket(incoming);

                LOGGER.Debug($"Sync OK");
                FeedbackService.Instance.Vibrate(TimeSpan.FromMilliseconds(200), 50);
                Synced?.Invoke(this, EventArgs.Empty);
            }
            catch (Exception ex) {
                LOGGER.Error("Sync error:", ex);
                try {
                    recordBlock?.Return();
                }
                catch (Exception ex1) {
                    LOGGER.Error("Record block (Return) error:", ex1);
                    throw;
                }
                throw;
            }
        }

        public Task ForceSync()
        {
            return Sync(_policyService.CurrentMonitoringPolicy?.PacketSize ?? 10);
        }

        public event EventHandler Synced;

        private void Reconfigure(MonitoringPolicy policy)
        {
            lock (_sync) {
                Unsubscribe();
                if(policy == null)
                    return;

                _subscription = Observable.Interval(TimeSpan.FromSeconds(policy.SyncInterval), TaskPoolScheduler.Default)
                                          .Subscribe(_ => Sync(policy.PacketSize));
            }
        }

        public void Start()
        {
            lock (_sync) {
                if (_subscription == null) {
                    _policyService.MonitoringPolicyChanged += HandleMonitoringPolicyChanged;
                    Reconfigure(_policyService.CurrentMonitoringPolicy);
                    LOGGER.Debug("Started");
                }
            }
        }

        public void Pause()
        {
            throw new NotImplementedException();
        }

        public void Stop()
        {
            lock (_sync) {
                if (_subscription != null) {
                    Unsubscribe();
                    _policyService.MonitoringPolicyChanged -= HandleMonitoringPolicyChanged;
                    LOGGER.Debug("Stopped");
                }
            }
        }
    }
}
