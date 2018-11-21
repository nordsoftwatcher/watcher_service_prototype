package ru.nord.siwatch.backend.services.supervisor.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nord.siwatch.backend.services.common.api.ApiBase;
import ru.nord.siwatch.backend.services.supervisor.api.dto.SupervisorDto;
import ru.nord.siwatch.backend.services.supervisor.entities.Supervisor;
import ru.nord.siwatch.backend.services.supervisor.mapping.SupervisorMapper;
import ru.nord.siwatch.backend.services.supervisor.repositories.SupervisorRepository;

@Api(description = "Supervisor API")
@RestController
@RequestMapping(value = ApiBase.PATH + SupervisorApi.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class SupervisorApi extends ApiBase {

    public static final String PATH = "supervisor";

    @Autowired
    private SupervisorRepository supervisorRepository;

    @Autowired
    private SupervisorMapper supervisorMapper;

    @ApiOperation(value = "Получение супервайзера по id")
    @GetMapping("/{supervisorId}")
    public SupervisorDto getSupervisor(@PathVariable Long supervisorId) {
        Supervisor supervisor = supervisorRepository.getSupervisorById(supervisorId);
        return supervisor != null ? supervisorMapper.toSupervisorDto(supervisor) : null;
    }

    @ApiOperation(value = "Получение супервайзера по id устройства")
    @GetMapping("/by_device_id/{deviceId}")
    public SupervisorDto getSupervisorByDeviceId(@PathVariable String deviceId) {
        Supervisor supervisor = supervisorRepository.getSupervisorByDeviceId(deviceId);
        return supervisor != null ? supervisorMapper.toSupervisorDto(supervisor) : null;
    }

}
