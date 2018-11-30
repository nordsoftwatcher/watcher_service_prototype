using System;
using System.Linq;
using System.Reactive.Concurrency;
using System.Reactive.Linq;
using System.Threading;
using System.Threading.Tasks;
using SiWatchApp.Buffer;
using SiWatchApp.Configuration;
using SiWatchApp.Logging;
using SiWatchApp.Models;

// ReSharper disable InconsistentlySynchronizedField

namespace SiWatchApp.Services
{
    public class SyncService : IDisposable
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger(nameof(SyncService));

        private readonly object _sync = new object();

        private readonly MonitoringPolicyService _policyService;
        private readonly IBuffer<Record> _buffer;
        private readonly Settings _settings;
        private readonly IObserver<EventRecord> _incomingEventsObserver;
        private readonly ISyncProxy _syncProxy;
        private CancellationTokenSource _cts;

        public SyncService(MonitoringPolicyService policyService,
                IBuffer<Record> buffer,
                ISyncProxy syncProxy,
                Settings settings,
                IObserver<EventRecord> incomingEventsObserver = null)
        {
            _buffer = buffer;
            _settings = settings;
            _incomingEventsObserver = incomingEventsObserver;
            _policyService = policyService;
            _syncProxy = syncProxy;
        }

        private void OnMonitoringPolicyChanged(object sender, MonitoringPolicy mp)
        {
            Reconfigure(mp);
        }
        
        private void Cancel()
        {
            if (_cts != null) {
                _cts.Cancel();
                _cts = null;
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

        private void ProcessResponsePacket(SyncPacket responsePacket)
        {
            if (responsePacket != null && _incomingEventsObserver != null) {
                if (responsePacket.Events != null && responsePacket.Events.Count > 0) {
                    LOGGER.Debug("Delegate incoming events");
                    foreach (var incomingEvent in responsePacket.Events) {
                        _incomingEventsObserver.OnNext(incomingEvent);
                    }
                }
            }
        }

        private async Task Sync(int packetSize)
        {
            LOGGER.Debug("Begin sync");

            SyncPacket incoming = null;
            IBlock<Record> recordBlock = null;
            try {
                LOGGER.Debug($"Fetching at most {packetSize} records from buffer...");
                recordBlock = _buffer.Get(packetSize);
    
                var outgoing = PreparePacket(recordBlock);

                LOGGER.Debug("Sync via proxy...");
                incoming = await _syncProxy.Sync(outgoing);

                try {
                    recordBlock?.Discard();
                }
                catch (Exception ex) {
                    LOGGER.Error("Record block (Discard) error:", ex);
                    throw;
                }
                recordBlock = null;

                LOGGER.Debug("Sync done");
                Synced?.Invoke(this, true);
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
                Synced?.Invoke(this, false);
            }

            if (incoming != null) {
                ProcessResponsePacket(incoming);
            }
        }

        public Task ForceSync()
        {
            return Sync(_policyService.CurrentMonitoringPolicy?.PacketSize ?? _settings.DefaultSyncPacketSize);
        }

        public event EventHandler<bool> Synced;
        
        private void Reconfigure(MonitoringPolicy policy)
        {
            lock (_sync) {
                Cancel();
                if (policy == null) {
                    return;
                }

                _cts = new CancellationTokenSource();

                var cancellationToken = _cts.Token;
                Task.Run(async () => {
                        while (!cancellationToken.IsCancellationRequested)
                        {
                            await Task.Delay(TimeSpan.FromSeconds(policy.SyncInterval), cancellationToken);
                            await Sync(policy.PacketSize);
                        }
                    }, cancellationToken);

                //_subscription = Observable
                //                .Interval(TimeSpan.FromSeconds(policy.SyncInterval), TaskPoolScheduler.Default)
                //                .Subscribe(_ => Sync(policy.PacketSize).Wait());
            }
        }

        public void Start()
        {
            lock (_sync) {
                if (_cts == null) {
                    _policyService.MonitoringPolicyChanged += OnMonitoringPolicyChanged;
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
                if (_cts != null) {
                    Cancel();
                    _policyService.MonitoringPolicyChanged -= OnMonitoringPolicyChanged;
                    LOGGER.Debug("Stopped");
                }
            }
        }

        public void Dispose()
        {
            lock (_sync) {
                if (_cts != null) {
                    Cancel();
                    _policyService.MonitoringPolicyChanged -= OnMonitoringPolicyChanged;
                }
                LOGGER.Debug("Disposed");
            }
        }
    }
}
