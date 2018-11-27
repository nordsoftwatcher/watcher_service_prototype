package ru.nord.siwatch.backend.facade.device.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.connectors.event.EventConnector;
import ru.nord.siwatch.backend.connectors.event.models.BusinessEventType;
import ru.nord.siwatch.backend.connectors.event.models.EventInfo;
import ru.nord.siwatch.backend.connectors.event.models.RouteEventType;
import ru.nord.siwatch.backend.connectors.heartratemonitoring.models.HeartRateInfo;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.LocationInfo;
import ru.nord.siwatch.backend.connectors.supervisor.SupervisorConnector;
import ru.nord.siwatch.backend.connectors.supervisor.model.Supervisor;
import ru.nord.siwatch.backend.facade.device.models.events.*;
import ru.nord.siwatch.backend.facade.device.models.monitoring.MonitorRecord;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BusinessEventService
{
    private final EventConnector eventConnector;
    private final SupervisorConnector supervisorConnector;
    private final HeartRateEventDetector heartRateEventDetector;
    private final MonitorRecordMapper monitorRecordMapper;

    public BusinessEventService(EventConnector eventConnector,
                                SupervisorConnector supervisorConnector,
                                HeartRateEventDetector heartRateEventDetector,
                                MonitorRecordMapper monitorRecordMapper)
    {
        this.eventConnector = eventConnector;
        this.supervisorConnector = supervisorConnector;
        this.heartRateEventDetector = heartRateEventDetector;
        this.monitorRecordMapper = monitorRecordMapper;
    }

    public void processEvents(String deviceId, List<MonitorRecord> monitors, List<EventRecord> events)
    {
        final Supervisor supervisor = supervisorConnector.getSupervisorByDeviceId(deviceId);

        // генерация событий, явно полученных с устройства
        if(events != null && !events.isEmpty()) {
            for (EventRecord eventRecord : events) {
                if (eventRecord.getEventType() == DeviceEventType.SOS) {
                    final EventInfo sosEvent = new EventInfo();
                    sosEvent.setEventType(BusinessEventType.SOS.name());
                    sosEvent.setEventValue(Objects.toString(eventRecord.getValue()));
                    sosEvent.setSupervisorId(supervisor.getId());
                    sosEvent.setDeviceTime(eventRecord.getTimestamp());
                    if (eventRecord.getLocation() != null) {
                        sosEvent.setLongitude(eventRecord.getLocation().getLongitude());
                        sosEvent.setLatitude(eventRecord.getLocation().getLatitude());
                    }
                    eventConnector.save(sosEvent);
                }
                else if (eventRecord.getEventType() == DeviceEventType.Action) {
                    final String value = Objects.toString(eventRecord.getValue(),"");
                    if (RouteEventType.RouteStart.name().equals(value) || RouteEventType.RouteFinish.name().equals(value)) {
                        final EventInfo routeEvent = new EventInfo();
                        routeEvent.setEventType(BusinessEventType.Route.name());
                        routeEvent.setEventValue(RouteEventType.valueOf(value).toString());
                        routeEvent.setSupervisorId(supervisor.getId());
                        routeEvent.setDeviceTime(eventRecord.getTimestamp());
                        if (eventRecord.getLocation() != null) {
                            routeEvent.setLongitude(eventRecord.getLocation().getLongitude());
                            routeEvent.setLatitude(eventRecord.getLocation().getLatitude());
                        }
                        eventConnector.save(routeEvent);
                    }
                }
            }
        }

        // генерация событий, основанных на показаниях сенсоров
        if (monitors != null && !monitors.isEmpty()) {
            monitors = monitors.stream().sorted(Comparator.comparing(MonitorRecord::getTimestamp)).collect(Collectors.toList());

            HeartRateInfo heartRateInfo = null;
            LocationInfo locationInfo = null;
            for (final MonitorRecord record : monitors) {
                Object info = monitorRecordMapper.getMonitorValueInfo(deviceId, record);
                if (info == null) {
                    log.warn("Unknown monitor record: " + record);
                } else if (info instanceof HeartRateInfo) {
                    heartRateInfo = (HeartRateInfo) info;
                } else if (info instanceof LocationInfo) {
                    locationInfo = (LocationInfo) info;
                }
            }

            final EventInfo heartRateEvent = heartRateEventDetector.detectEvent(supervisor, heartRateInfo, locationInfo);
            if (heartRateEvent != null) {
                eventConnector.save(heartRateEvent);
            }
        }
    }
}
