package ru.nord.siwatch.backend.services.events.mapping;

import org.mapstruct.Mapper;
import ru.nord.siwatch.backend.services.events.api.dto.EventDto;
import ru.nord.siwatch.backend.services.events.api.model.CreateEventInput;
import ru.nord.siwatch.backend.services.events.entities.Event;
import java.util.List;

@Mapper
public interface EventMapper {

    Event toEvent(CreateEventInput createEventInput);

    EventDto toEventDto(Event event);

    List<EventDto> toEventDtoList(List<Event> events);

}
