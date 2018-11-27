package ru.nord.siwatch.backend.services.events.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.services.events.api.model.CreateEventInput;
import ru.nord.siwatch.backend.services.events.entities.Event;
import ru.nord.siwatch.backend.services.events.mapping.EventMapper;
import ru.nord.siwatch.backend.services.events.repositories.EventRepository;
import ru.nord.siwatch.backend.services.events.services.EventService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    public Event getLastEventByTypeAndSupervisorId(String eventType, Long supervisorId) {
        return eventRepository.findFirstByEventTypeAndSupervisorIdOrderByDeviceTimeDesc(eventType, supervisorId).orElse(null);
    }

    @Override
    public List<Event> findEvents(Long supervisorId, Date from, Date to) {
        if (supervisorId == null) {
            return findEvents(from, to);
        }
        if (from != null && to != null) {
            return eventRepository.findAllBySupervisorIdAndInInterval(supervisorId, from, to);
        }
        if (from != null && to == null) {
            return eventRepository.findAllBySupervisorIdAndFromDate(supervisorId, from);
        }
        if (from == null && to != null) {
            return eventRepository.findAllBySupervisorIdAndToDate(supervisorId, to);
        }
        return eventRepository.findAllBySupervisor(supervisorId);
    }

    @Override
    public List<Event> findEvents(Date from, Date to) {
        if (from != null && to != null) {
            return eventRepository.findAllInInterval(from, to);
        }
        if (from != null && to == null) {
            return eventRepository.findAllByFromDate(from);
        }
        if (from == null && to != null) {
            return eventRepository.findAllByToDate(to);
        }
        return eventRepository.findAllEvents();
    }
}
