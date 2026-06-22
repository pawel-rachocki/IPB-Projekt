package pl.pjatk.ipb.deployment.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjatk.ipb.deployment.domain.PortalDeployment;
import pl.pjatk.ipb.deployment.domain.UatVerification;
import pl.pjatk.ipb.deployment.domain.enums.UatStatus;
import pl.pjatk.ipb.deployment.repository.PortalDeploymentRepository;
import pl.pjatk.ipb.deployment.repository.UatVerificationRepository;
import pl.pjatk.ipb.deployment.web.NotFoundException;
import pl.pjatk.ipb.deployment.web.dto.CreateVerificationRequest;
import pl.pjatk.ipb.deployment.web.dto.DomainMapper;
import pl.pjatk.ipb.deployment.web.dto.UatVerificationDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UatVerificationService {

    private static final Set<String> SORT_FIELDS =
            Set.of("verificationId", "verificationDate");

    private final UatVerificationRepository repository;
    private final PortalDeploymentRepository deploymentRepository;
    private final DomainMapper mapper;

    public UatVerificationService(UatVerificationRepository repository,
                                  PortalDeploymentRepository deploymentRepository,
                                  DomainMapper mapper) {
        this.repository = repository;
        this.deploymentRepository = deploymentRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<UatVerificationDto> list(Long deploymentId, String sort) {
        Sort sortSpec = SortSupport.parse(sort, SORT_FIELDS, "verificationDate");
        return repository.findAll(sortSpec).stream()
                .filter(v -> deploymentId == null
                        || (v.getDeployment() != null
                        && deploymentId.equals(v.getDeployment().getDeploymentId())))
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public UatVerificationDto get(Long id) {
        return mapper.toDto(find(id));
    }

    public UatVerificationDto createForDeployment(Long deploymentId, CreateVerificationRequest request) {
        PortalDeployment deployment = deploymentRepository.findById(deploymentId)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono wdrozenia o id=" + deploymentId));

        UatVerification verification = new UatVerification();
        verification.setDeployment(deployment);
        verification.setRequirementsMet(request.requirementsMet());
        verification.setNoOperationalImpact(request.noOperationalImpact());
        verification.setSecurityAuditPassed(request.securityAuditPassed());
        verification.setRejectionJustification(request.rejectionJustification());
        verification.setVerificationDate(LocalDateTime.now());

        boolean accepted = request.requirementsMet()
                && request.noOperationalImpact()
                && request.securityAuditPassed();
        if (accepted) {
            deployment.setUatStatus(UatStatus.ACCEPTED);
            deployment.setApprovedProdDate(LocalDateTime.now());
        } else {
            deployment.setUatStatus(UatStatus.REJECTED);
        }

        return mapper.toDto(repository.save(verification));
    }

    private UatVerification find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono weryfikacji o id=" + id));
    }
}
