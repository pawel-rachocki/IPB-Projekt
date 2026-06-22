package pl.pjatk.ipb.deployment.web.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CreateCycleRequest(
        @NotBlank String targetVersionTag,
        LocalDateTime plannedDeploymentWindow
) {
}
