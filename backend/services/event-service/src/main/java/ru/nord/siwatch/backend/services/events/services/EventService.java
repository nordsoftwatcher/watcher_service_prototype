package ru.nord.siwatch.backend.services.events.services;

import ru.nord.siwatch.backend.services.events.api.model.CreateEventInput;
import ru.nord.siwatch.backend.services.events.entities.Event;

import java.util.Date;
import java.util.List;

public interface EventService {

    Event createEvent(CreateEventInput createEventInput);

    List<Event> findEvents(Long supervisorId, Date from, Date to);

    List<Event> findEvents(Date from, Date to);

}
