package pl.pjatk.ipb.deployment.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.pjatk.ipb.deployment.domain.enums.TicketType;

public record CreateTicketRequest(
        @NotBlank String ticketId,
        Long cycleId,
        @NotBlank String title,
        String requirementDescription,
        @NotNull TicketType type,
        Long assigneeId
) {
}
