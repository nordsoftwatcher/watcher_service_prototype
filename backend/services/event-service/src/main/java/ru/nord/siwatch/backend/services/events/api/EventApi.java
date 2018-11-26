package ru.nord.siwatch.backend.services.events.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nord.siwatch.backend.services.common.api.ApiBase;
import ru.nord.siwatch.backend.services.events.api.dto.EventDto;
import ru.nord.siwatch.backend.services.events.api.model.CreateEventInput;
import ru.nord.siwatch.backend.services.events.entities.Event;
import ru.nord.siwatch.backend.services.events.mapping.EventMapper;
import ru.nord.siwatch.backend.services.events.services.EventService;

import javax.validation.Valid;

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

}
