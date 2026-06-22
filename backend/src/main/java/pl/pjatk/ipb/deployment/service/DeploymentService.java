package pl.pjatk.ipb.deployment.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjatk.ipb.deployment.domain.PortalDeployment;
import pl.pjatk.ipb.deployment.domain.ReleaseCycle;
import pl.pjatk.ipb.deployment.domain.enums.DeploymentStatus;
import pl.pjatk.ipb.deployment.domain.enums.UatStatus;
import pl.pjatk.ipb.deployment.repository.PortalDeploymentRepository;
import pl.pjatk.ipb.deployment.repository.ReleaseCycleRepository;
import pl.pjatk.ipb.deployment.web.NotFoundException;
import pl.pjatk.ipb.deployment.web.dto.CreateDeploymentRequest;
import pl.pjatk.ipb.deployment.web.dto.DeploymentDto;
import pl.pjatk.ipb.deployment.web.dto.DomainMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class DeploymentService {

    private static final Set<String> SORT_FIELDS =
            Set.of("deploymentId", "uatStatus", "deploymentStatus", "approvedProdDate");

    private final PortalDeploymentRepository repository;
    private final ReleaseCycleRepository cycleRepository;
    private final DomainMapper mapper;

    public DeploymentService(PortalDeploymentRepository repository, ReleaseCycleRepository cycleRepository,
                             DomainMapper mapper) {
        this.repository = repository;
        this.cycleRepository = cycleRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<DeploymentDto> list(DeploymentStatus deploymentStatus, UatStatus uatStatus, String sort) {
        Sort sortSpec = SortSupport.parse(sort, SORT_FIELDS, "deploymentId");
        return repository.findAll(sortSpec).stream()
                .filter(d -> deploymentStatus == null || d.getDeploymentStatus() == deploymentStatus)
                .filter(d -> uatStatus == null || d.getUatStatus() == uatStatus)
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public DeploymentDto get(Long id) {
        return mapper.toDto(find(id));
    }

    public DeploymentDto create(CreateDeploymentRequest request) {
        ReleaseCycle cycle = cycleRepository.findById(request.cycleId())
                .orElseThrow(() -> new NotFoundException("Nie znaleziono cyklu o id=" + request.cycleId()));
        PortalDeployment deployment = new PortalDeployment();
        deployment.setCycle(cycle);
        deployment.setUatEnvironmentUrl(request.uatEnvironmentUrl());
        deployment.setUatStatus(UatStatus.PENDING);
        deployment.setDeploymentStatus(DeploymentStatus.NOT_CREATED);
        return mapper.toDto(repository.save(deployment));
    }

    public DeploymentDto changeUatStatus(Long id, UatStatus newStatus) {
        PortalDeployment deployment = find(id);
        deployment.setUatStatus(newStatus);
        if (newStatus == UatStatus.ACCEPTED) {
            deployment.setApprovedProdDate(LocalDateTime.now());
        }
        return mapper.toDto(deployment);
    }

    public DeploymentDto changeDeploymentStatus(Long id, DeploymentStatus newStatus) {
        PortalDeployment deployment = find(id);
        deployment.setDeploymentStatus(newStatus);
        return mapper.toDto(deployment);
    }

    private PortalDeployment find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono wdrozenia o id=" + id));
    }
}
