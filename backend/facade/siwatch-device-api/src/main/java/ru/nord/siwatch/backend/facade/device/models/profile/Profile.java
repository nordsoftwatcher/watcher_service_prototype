package ru.nord.siwatch.backend.facade.device.models.profile;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Profile
{
    private List<MonitoringPolicy> monitoring;
}
