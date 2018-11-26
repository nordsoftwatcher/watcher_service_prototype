package ru.nord.siwatch.backend.services.events.services;

import ru.nord.siwatch.backend.services.events.api.model.CreateEventInput;
import ru.nord.siwatch.backend.services.events.entities.Event;

public interface EventService {

    Event createEvent(CreateEventInput createEventInput);

}
