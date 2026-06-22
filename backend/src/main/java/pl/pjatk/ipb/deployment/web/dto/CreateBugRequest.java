package pl.pjatk.ipb.deployment.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.pjatk.ipb.deployment.domain.enums.BugPriority;
import pl.pjatk.ipb.deployment.domain.enums.BugSource;

public record CreateBugRequest(
        @NotBlank String bugId,
        @NotBlank String defectDescription,
        @NotNull BugPriority priority,
        @NotNull BugSource source
) {
}
