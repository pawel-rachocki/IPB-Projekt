package pl.pjatk.ipb.deployment.web.dto;

import jakarta.validation.constraints.NotNull;

public record CreateDeploymentRequest(
        @NotNull Long cycleId,
        String uatEnvironmentUrl
) {
}
