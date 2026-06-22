package pl.pjatk.ipb.deployment.web.dto;

import pl.pjatk.ipb.deployment.domain.enums.RnViewType;

import java.time.LocalDateTime;

public record ReleaseNotesDto(
        Long rnId,
        Long buildId,
        RnViewType viewType,
        String textContent,
        LocalDateTime lastUpdatedDate
) {
}
