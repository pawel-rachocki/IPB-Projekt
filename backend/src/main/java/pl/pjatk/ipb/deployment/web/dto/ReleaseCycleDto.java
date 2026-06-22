package pl.pjatk.ipb.deployment.web.dto;

import pl.pjatk.ipb.deployment.domain.enums.CycleStatus;

import java.time.LocalDateTime;

public record ReleaseCycleDto(
        Long cycleId,
        String targetVersionTag,
        LocalDateTime startDate,
        LocalDateTime plannedDeploymentWindow,
        CycleStatus status
) {
}
