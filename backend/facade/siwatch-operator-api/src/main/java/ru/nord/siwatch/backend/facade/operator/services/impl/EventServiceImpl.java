package ru.nord.siwatch.backend.facade.operator.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.connectors.event.EventConnector;
import ru.nord.siwatch.backend.connectors.event.models.Event;
import ru.nord.siwatch.backend.facade.operator.services.EventService;

import java.time.LocalDateTime;
import java.util.List;

@Service("eventService")
public class EventServiceImpl implements EventService {

    @Autowired
    private EventConnector eventConnector;

    @Override
    public List<Event> findEvents(Long supervisorId, LocalDateTime from, LocalDateTime to) {
        return eventConnector.find(supervisorId, from, to);
    }
}
