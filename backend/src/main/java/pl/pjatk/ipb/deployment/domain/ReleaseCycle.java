package pl.pjatk.ipb.deployment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pjatk.ipb.deployment.domain.enums.CycleStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "release_cycle")
@Getter
@Setter
@NoArgsConstructor
public class ReleaseCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cycle_id")
    private Long cycleId;

    @Column(name = "target_version_tag", nullable = false, length = 50)
    private String targetVersionTag;

    @Column(name = "start_date")
    private LocalDateTime startDate = LocalDateTime.now();

    @Column(name = "planned_deployment_window")
    private LocalDateTime plannedDeploymentWindow;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_code", nullable = false, length = 50)
    private CycleStatus status = CycleStatus.PLANNED;
}
