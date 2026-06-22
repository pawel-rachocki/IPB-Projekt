package pl.pjatk.ipb.deployment.web.dto;

public record CreateVerificationRequest(
        boolean requirementsMet,
        boolean noOperationalImpact,
        boolean securityAuditPassed,
        String rejectionJustification
) {
}
