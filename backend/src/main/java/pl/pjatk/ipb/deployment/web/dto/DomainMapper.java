package pl.pjatk.ipb.deployment.web.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.pjatk.ipb.deployment.domain.BugTicket;
import pl.pjatk.ipb.deployment.domain.ItsTicket;
import pl.pjatk.ipb.deployment.domain.PortalDeployment;
import pl.pjatk.ipb.deployment.domain.ReleaseCycle;
import pl.pjatk.ipb.deployment.domain.ReleaseNotes;
import pl.pjatk.ipb.deployment.domain.SoftwareBuild;
import pl.pjatk.ipb.deployment.domain.UatVerification;

@Mapper(componentModel = "spring")
public interface DomainMapper {

    ReleaseCycleDto toDto(ReleaseCycle entity);

    @Mapping(target = "cycleId", source = "cycle.cycleId")
    TicketDto toDto(ItsTicket entity);

    @Mapping(target = "parentTicketId", source = "parentTicket.ticketId")
    BugDto toDto(BugTicket entity);

    @Mapping(target = "cycleId", source = "cycle.cycleId")
    BuildDto toDto(SoftwareBuild entity);

    @Mapping(target = "buildId", source = "build.buildId")
    ReleaseNotesDto toDto(ReleaseNotes entity);

    @Mapping(target = "cycleId", source = "cycle.cycleId")
    DeploymentDto toDto(PortalDeployment entity);

    @Mapping(target = "deploymentId", source = "deployment.deploymentId")
    UatVerificationDto toDto(UatVerification entity);
}
