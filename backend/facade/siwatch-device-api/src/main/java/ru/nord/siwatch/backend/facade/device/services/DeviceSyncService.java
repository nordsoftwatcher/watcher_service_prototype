package ru.nord.siwatch.backend.facade.device.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.connectors.batterymonitoring.BatteryMonitoringConnector;
import ru.nord.siwatch.backend.connectors.batterymonitoring.models.BatteryLevelInfo;
import ru.nord.siwatch.backend.connectors.heartratemonitoring.HeartRateMonitoringConnector;
import ru.nord.siwatch.backend.connectors.heartratemonitoring.models.HeartRateInfo;
import ru.nord.siwatch.backend.connectors.locationmonitoring.LocationMonitoringConnector;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.LocationInfo;
import ru.nord.siwatch.backend.connectors.memorymonitoring.MemoryMonitoringConnector;
import ru.nord.siwatch.backend.connectors.memorymonitoring.models.MemoryInfo;
import ru.nord.siwatch.backend.connectors.networkmonitoring.NetworkMonitoringConnector;
import ru.nord.siwatch.backend.connectors.networkmonitoring.models.NetworkInfo;
import ru.nord.siwatch.backend.facade.device.models.MessagePacket;
import ru.nord.siwatch.backend.facade.device.models.SyncPacket;
import ru.nord.siwatch.backend.facade.device.models.events.DeviceEventType;
import ru.nord.siwatch.backend.facade.device.models.events.EventPriority;
import ru.nord.siwatch.backend.facade.device.models.events.EventRecord;
import ru.nord.siwatch.backend.facade.device.models.monitoring.MonitorRecord;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class DeviceSyncService
{
    private final BatteryMonitoringConnector batteryMonitoringConnector;
    private final HeartRateMonitoringConnector heartRateMonitoringConnector;
    private final NetworkMonitoringConnector networkMonitoringConnector;
    private final MemoryMonitoringConnector memoryMonitoringConnector;
    private final LocationMonitoringConnector locationMonitoringConnector;
    private final BusinessEventService businessEventService;
    private final MonitorRecordMapper monitorRecordMapper;

    public DeviceSyncService(
        BatteryMonitoringConnector batteryMonitoringConnector,
        HeartRateMonitoringConnector heartRateMonitoringConnector,
        NetworkMonitoringConnector networkMonitoringConnector,
        MemoryMonitoringConnector memoryMonitoringConnector,
        LocationMonitoringConnector locationMonitoringConnector,
        BusinessEventService businessEventService,
        MonitorRecordMapper monitorRecordMapper)
    {
        this.batteryMonitoringConnector = batteryMonitoringConnector;
        this.heartRateMonitoringConnector = heartRateMonitoringConnector;
        this.networkMonitoringConnector = networkMonitoringConnector;
        this.memoryMonitoringConnector = memoryMonitoringConnector;
        this.locationMonitoringConnector = locationMonitoringConnector;
        this.businessEventService = businessEventService;
        this.monitorRecordMapper = monitorRecordMapper;
    }

    private List<EventRecord> getOutgoingEvents() {
        List<EventRecord> outgoingEvents = new ArrayList<>();
        if(Math.random() < 0.3) {
            EventRecord eventRecord = new EventRecord();
            eventRecord.setEventType(DeviceEventType.Message);
            eventRecord.setTimestamp(LocalDateTime.now());
            eventRecord.setPriority(EventPriority.Low);
            eventRecord.setValue("Test message at "+ new Date());
            outgoingEvents.add(eventRecord);
        }
        return outgoingEvents;
    }

    private void processMonitors(String deviceId, List<MonitorRecord> monitors)
    {
        if(monitors == null || monitors.isEmpty())
            return;

        for (final MonitorRecord record : monitors) {
            Object info = monitorRecordMapper.getMonitorValueInfo(deviceId, record);
            if(info == null) {
                log.warn("Unknown monitor record: "+record);
            } else {
                try {
                    if (info instanceof BatteryLevelInfo) {
                        batteryMonitoringConnector.save((BatteryLevelInfo) info);
                    } else if (info instanceof HeartRateInfo) {
                        heartRateMonitoringConnector.save((HeartRateInfo) info);
                    } else if (info instanceof MemoryInfo) {
                        memoryMonitoringConnector.save((MemoryInfo) info);
                    } else if (info instanceof NetworkInfo) {
                        networkMonitoringConnector.save((NetworkInfo) info);
                    } else if (info instanceof LocationInfo) {
                        locationMonitoringConnector.save((LocationInfo) info);
                    } else {
                        log.warn("Unknown monitor value type: " + info.getClass().getName());
                    }
                } catch (Exception ex) {
                    log.error("Failed saving '" + record.getMonitorType() + "' monitor value", ex);
                }
            }
        }
    }

    private void processEvents(String deviceId, List<MonitorRecord> monitors, List<EventRecord> events)
    {
        try {
            businessEventService.processEvents(deviceId, monitors, events);
        }
        catch (Exception ex) {
            log.error("Failed processing events", ex);
        }
    }

    public MessagePacket sync(SyncPacket packet)
    {
        final String deviceId = packet.getDeviceId();

        processMonitors(deviceId, packet.getMonitors());
        processEvents(deviceId, packet.getMonitors(), packet.getEvents());

        MessagePacket response = new MessagePacket();
        response.setEvents(getOutgoingEvents());
        response.setTimestamp(LocalDateTime.now());
        
        return response;
    }
}
