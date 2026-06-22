package pl.pjatk.ipb.deployment.web.dto;

import jakarta.validation.constraints.NotNull;
import pl.pjatk.ipb.deployment.domain.enums.RnViewType;

public record UpdateReleaseNotesRequest(
        @NotNull RnViewType viewType,
        String textContent
) {
}
