package pl.pjatk.ipb.deployment.web.dto;

import pl.pjatk.ipb.deployment.domain.enums.DeploymentStatus;
import pl.pjatk.ipb.deployment.domain.enums.UatStatus;

import java.time.LocalDateTime;

public record DeploymentDto(
        Long deploymentId,
        Long cycleId,
        String uatEnvironmentUrl,
        UatStatus uatStatus,
        LocalDateTime approvedProdDate,
        DeploymentStatus deploymentStatus
) {
}
