package pl.pjatk.ipb.deployment.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBuildRequest(
        @NotNull Long cycleId,
        @NotBlank String commitHash,
        Boolean staticAnalysisPassed
) {
}
