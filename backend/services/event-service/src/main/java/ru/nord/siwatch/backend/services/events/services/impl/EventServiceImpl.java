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

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Event createEvent(CreateEventInput createEventInput) {
        Event event = eventMapper.toEvent(createEventInput);
        return eventRepository.save(event);
    }

    @Override
    public Event getLastEventByTypeAndSupervisorId(String eventType, Long supervisorId) {
        try {
            Query query = entityManager.createQuery("SELECT event FROM Event event " +
                    "WHERE event.eventType = :eventType and event.supervisorId = :supervisorId " +
                    "ORDER BY event.deviceTime DESC");
            query.setParameter("eventType", eventType);
            query.setParameter("supervisorId", supervisorId);
            query.setFirstResult(0);
            query.setMaxResults(1);
            return (Event) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
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
