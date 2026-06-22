package pl.pjatk.ipb.deployment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pjatk.ipb.deployment.domain.enums.TicketStatus;
import pl.pjatk.ipb.deployment.domain.enums.TicketType;

@Entity
@Table(name = "its_ticket")
@Getter
@Setter
@NoArgsConstructor
public class ItsTicket {

    @Id
    @Column(name = "ticket_id", length = 50)
    private String ticketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_id")
    private ReleaseCycle cycle;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "requirement_description")
    private String requirementDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_code", nullable = false, length = 50)
    private TicketType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_code", nullable = false, length = 50)
    private TicketStatus status = TicketStatus.TO_DO;

    @Column(name = "assignee_id")
    private Long assigneeId;
}
