package ru.nord.siwatch.backend.facade.operator.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nord.siwatch.backend.connectors.supervisor.SupervisorConnector;
import ru.nord.siwatch.backend.connectors.supervisor.model.Supervisor;
import ru.nord.siwatch.backend.facade.operator.services.SupervisorService;

@Service("supervisorService")
public class SupervisorServiceImpl implements SupervisorService {

    @Autowired
    private SupervisorConnector supervisorConnector;

    @Override
    public Supervisor getSupervisorById(Long supervisorId) {
        return supervisorConnector.getSupervisorById(supervisorId);
    }

    @Override
    public Supervisor getSupervisorByDeviceId(String deviceId) {
        return supervisorConnector.getSupervisorByDeviceId(deviceId);
    }
}
