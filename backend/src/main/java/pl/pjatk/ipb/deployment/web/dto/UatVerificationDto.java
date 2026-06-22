package pl.pjatk.ipb.deployment.web.dto;

import java.time.LocalDateTime;

public record UatVerificationDto(
        Long verificationId,
        Long deploymentId,
        boolean requirementsMet,
        boolean noOperationalImpact,
        boolean securityAuditPassed,
        String rejectionJustification,
        LocalDateTime verificationDate
) {
}
