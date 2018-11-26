package ru.nord.siwatch.backend.facade.operator.services;

import ru.nord.siwatch.backend.connectors.event.models.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<Event> findEvents(Long supervisorId, LocalDateTime from, LocalDateTime to);

}
