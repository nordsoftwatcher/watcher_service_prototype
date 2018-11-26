package ru.nord.siwatch.backend.facade.operator.api.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nord.siwatch.backend.connectors.event.models.Event;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.SearchEventsDto;
import ru.nord.siwatch.backend.facade.operator.services.EventService;

import java.util.List;

@Api(description = "OperatorEventApi")
@RestController
@RequestMapping(value = ApiBase.PATH + OperatorEventApi.PATH)
@Slf4j
public class OperatorEventApi extends ApiBase {

    public static final String PATH = "operator/event";

    @Autowired
    private EventService eventService;

    @ApiOperation(value = "Получение событий за временной интервал")
    @GetMapping
    public List<Event> getEvent(SearchEventsDto searchEventsDto) {
        return eventService.findEvents(searchEventsDto.getSupervisorId(), searchEventsDto.getFromTime(), searchEventsDto.getToTime());
    }


}
