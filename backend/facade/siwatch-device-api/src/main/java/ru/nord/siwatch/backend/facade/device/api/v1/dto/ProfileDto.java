package ru.nord.siwatch.backend.facade.device.api.v1.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ProfileDto
{
    private List<MonitoringPolicyDto> monitoring;
}
