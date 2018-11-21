package ru.nord.siwatch.backend.facade.operator.services;

import ru.nord.siwatch.backend.connectors.supervisor.model.Supervisor;

public interface SupervisorService {

    Supervisor getSupervisorById(Long supervisorId);

    Supervisor getSupervisorByDeviceId(String deviceId);

}
