package pl.pjatk.ipb.deployment.web.dto;

import pl.pjatk.ipb.deployment.domain.enums.TicketStatus;
import pl.pjatk.ipb.deployment.domain.enums.TicketType;

public record TicketDto(
        String ticketId,
        Long cycleId,
        String title,
        String requirementDescription,
        TicketType type,
        TicketStatus status,
        Long assigneeId
) {
}
