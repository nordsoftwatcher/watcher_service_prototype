package ru.nord.siwatch.backend.services.events.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.services.events.api.model.CreateEventInput;
import ru.nord.siwatch.backend.services.events.entities.Event;
import ru.nord.siwatch.backend.services.events.mapping.EventMapper;
import ru.nord.siwatch.backend.services.events.repositories.EventRepository;
import ru.nord.siwatch.backend.services.events.services.EventService;

import java.util.Date;
import java.util.List;

@Service("eventService")
public class EventServiceImpl implements EventService {

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private EventRepository eventRepository;

    @Override
    public Event createEvent(CreateEventInput createEventInput) {
        Event event = eventMapper.toEvent(createEventInput);
        return eventRepository.save(event);
    }

    @Override
    public List<Event> findEvents(Long supervisorId, Date from, Date to) {
        return null;
    }

    @Override
    public List<Event> findEvents(Date from, Date to) {
        return null;
    }
}
