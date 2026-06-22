package pl.pjatk.ipb.deployment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "uat_verification")
@Getter
@Setter
@NoArgsConstructor
public class UatVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verification_id")
    private Long verificationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "deployment_id", nullable = false)
    private PortalDeployment deployment;

    @Column(name = "requirements_met", nullable = false)
    private boolean requirementsMet;

    @Column(name = "no_operational_impact", nullable = false)
    private boolean noOperationalImpact;

    @Column(name = "security_audit_passed", nullable = false)
    private boolean securityAuditPassed;

    @Lob
    @Column(name = "rejection_justification")
    private String rejectionJustification;

    @Column(name = "verification_date")
    private LocalDateTime verificationDate = LocalDateTime.now();
}
