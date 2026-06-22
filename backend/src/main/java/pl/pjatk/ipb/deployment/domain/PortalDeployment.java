package pl.pjatk.ipb.deployment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pjatk.ipb.deployment.domain.enums.DeploymentStatus;
import pl.pjatk.ipb.deployment.domain.enums.UatStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "portal_deployment")
@Getter
@Setter
@NoArgsConstructor
public class PortalDeployment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deployment_id")
    private Long deploymentId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cycle_id", nullable = false, unique = true)
    private ReleaseCycle cycle;

    @Column(name = "uat_environment_url")
    private String uatEnvironmentUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "uat_status_code", nullable = false, length = 50)
    private UatStatus uatStatus = UatStatus.PENDING;

    @Column(name = "approved_prod_date")
    private LocalDateTime approvedProdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "deployment_status_code", nullable = false, length = 50)
    private DeploymentStatus deploymentStatus = DeploymentStatus.NOT_CREATED;
}
