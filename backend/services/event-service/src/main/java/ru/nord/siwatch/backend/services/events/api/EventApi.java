package ru.nord.siwatch.backend.services.events.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.nord.siwatch.backend.services.common.api.ApiBase;
import ru.nord.siwatch.backend.services.events.api.dto.EventDto;
import ru.nord.siwatch.backend.services.events.api.dto.SearchEventsDto;
import ru.nord.siwatch.backend.services.events.api.model.CreateEventInput;
import ru.nord.siwatch.backend.services.events.entities.Event;
import ru.nord.siwatch.backend.services.events.mapping.EventMapper;
import ru.nord.siwatch.backend.services.events.services.EventService;

import javax.validation.Valid;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@Api(description = "Event API")
@RestController
@RequestMapping(value = ApiBase.PATH + EventApi.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class EventApi extends ApiBase {

    public static final String PATH = "event";

    @Autowired
    private EventService eventService;

    @Autowired
    private EventMapper eventMapper;

    @ApiOperation(value = "Создание события")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public EventDto createEvent(@RequestBody @Valid CreateEventInput createEventInput) {
        Event event = eventService.createEvent(createEventInput);
        return eventMapper.toEventDto(event);
    }

    @ApiOperation(value = "Получение последнего события заданного типа")
    @GetMapping("/{eventType}/supervisor/{supervisorId}")
    public EventDto getLastEventByTypeAndSupervisorId(@PathVariable("eventType") String eventType, @PathVariable("supervisorId") Long supervisorId) {
        Event lastEvent = eventService.getLastEventByTypeAndSupervisorId(eventType, supervisorId);
        EventDto eventDto = eventMapper.toEventDto(lastEvent);
        return eventDto;
    }

    @ApiOperation(value = "Получение событий за временной интервал")
    @GetMapping("/search")
    public List<EventDto> search(SearchEventsDto searchEventsDto) {
        final List<Event> events = eventService.findEvents(
                searchEventsDto.getSupervisorId(),
                searchEventsDto.getFromTime() != null ? Date.from(searchEventsDto.getFromTime().toInstant(ZoneOffset.UTC)) : null,
                searchEventsDto.getToTime() != null ? Date.from(searchEventsDto.getToTime().toInstant(ZoneOffset.UTC)) : null
        );
        return eventMapper.toEventDtoList(events);
    }

}
