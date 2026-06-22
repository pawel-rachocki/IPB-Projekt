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
import pl.pjatk.ipb.deployment.domain.enums.BugPriority;
import pl.pjatk.ipb.deployment.domain.enums.BugSource;
import pl.pjatk.ipb.deployment.domain.enums.BugStatus;

@Entity
@Table(name = "bug_ticket")
@Getter
@Setter
@NoArgsConstructor
public class BugTicket {

    @Id
    @Column(name = "bug_id", length = 50)
    private String bugId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parent_ticket_id", nullable = false)
    private ItsTicket parentTicket;

    @Lob
    @Column(name = "defect_description", nullable = false)
    private String defectDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority_code", nullable = false, length = 50)
    private BugPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_code", nullable = false, length = 50)
    private BugSource source;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_code", nullable = false, length = 50)
    private BugStatus status = BugStatus.NEW;
}
