package ru.nord.siwatch.backend.facade.device.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.connectors.event.EventConnector;
import ru.nord.siwatch.backend.connectors.event.models.Event;
import ru.nord.siwatch.backend.connectors.event.models.EventInfo;
import ru.nord.siwatch.backend.connectors.heartratemonitoring.models.HeartRateInfo;
import ru.nord.siwatch.backend.connectors.locationmonitoring.models.LocationInfo;
import ru.nord.siwatch.backend.connectors.supervisor.model.Supervisor;
import ru.nord.siwatch.backend.connectors.event.models.BusinessEventType;
import ru.nord.siwatch.backend.connectors.event.models.HeartRateEventType;

import java.util.Objects;

@Service
@Slf4j
public class HeartRateEventDetector
{
    private final EventConnector eventConnector;

    public HeartRateEventDetector(EventConnector eventConnector)
    {
        this.eventConnector = eventConnector;
    }

    private EventInfo createEvent(Supervisor supervisor, HeartRateInfo heartRate, HeartRateEventType eventType, @Nullable LocationInfo location)
    {
        final EventInfo event = new EventInfo();
        event.setSupervisorId(supervisor.getId());
        event.setDeviceTime(heartRate.getDeviceTime());
        event.setEventType(BusinessEventType.HeartRate.name());
        event.setEventValue(eventType.name());
        if(location != null) {
            event.setLatitude(location.getLatitude());
            event.setLongitude(location.getLongitude());
        }
        return event;
    }

    public EventInfo detectEvent(Supervisor supervisor, HeartRateInfo heartRateInfo, @Nullable LocationInfo location)
    {
        final Event last = eventConnector.getLastEventByTypeAndSupervisorId(BusinessEventType.HeartRate.name(), supervisor.getId());

        if(heartRateInfo.getRate() < 1) {
            if(last == null || !HeartRateEventType.Stopped.name().equals(last.getEventValue())) {
                return createEvent(supervisor, heartRateInfo, HeartRateEventType.Stopped, location);
            }
        }
        else {
            if(last != null && HeartRateEventType.Stopped.name().equals(last.getEventValue())) {
                return createEvent(supervisor, heartRateInfo, HeartRateEventType.Resumed, location);
            }
        }
        return null;
    }
}
