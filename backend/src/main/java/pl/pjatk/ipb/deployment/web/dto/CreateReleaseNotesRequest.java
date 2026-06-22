package pl.pjatk.ipb.deployment.web.dto;

import jakarta.validation.constraints.NotNull;
import pl.pjatk.ipb.deployment.domain.enums.RnViewType;

public record CreateReleaseNotesRequest(
        @NotNull Long buildId,
        @NotNull RnViewType viewType,
        String textContent
) {
}
