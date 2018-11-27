package ru.nord.siwatch.backend.facade.device.models;

import lombok.Getter;
import lombok.Setter;
import ru.nord.siwatch.backend.facade.device.models.monitoring.MonitorRecord;

import java.util.List;

@Getter @Setter
public class SyncPacket extends MessagePacket
{
    private String deviceId;

    private List<MonitorRecord> monitors;
}
