package pl.pjatk.ipb.deployment.web.dto;

import pl.pjatk.ipb.deployment.domain.enums.BugPriority;
import pl.pjatk.ipb.deployment.domain.enums.BugSource;
import pl.pjatk.ipb.deployment.domain.enums.BugStatus;

public record BugDto(
        String bugId,
        String parentTicketId,
        String defectDescription,
        BugPriority priority,
        BugSource source,
        BugStatus status
) {
}
