package pl.pjatk.ipb.deployment.web.dto;

import java.time.LocalDateTime;

public record BuildDto(
        Long buildId,
        Long cycleId,
        String commitHash,
        LocalDateTime compilationDate,
        Boolean staticAnalysisPassed
) {
}
