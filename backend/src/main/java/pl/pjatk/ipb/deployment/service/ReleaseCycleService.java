package pl.pjatk.ipb.deployment.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjatk.ipb.deployment.domain.ReleaseCycle;
import pl.pjatk.ipb.deployment.domain.enums.CycleStatus;
import pl.pjatk.ipb.deployment.repository.ReleaseCycleRepository;
import pl.pjatk.ipb.deployment.web.NotFoundException;
import pl.pjatk.ipb.deployment.web.dto.CreateCycleRequest;
import pl.pjatk.ipb.deployment.web.dto.DomainMapper;
import pl.pjatk.ipb.deployment.web.dto.ReleaseCycleDto;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ReleaseCycleService {

    private static final Set<String> SORT_FIELDS =
            Set.of("cycleId", "targetVersionTag", "startDate", "status");

    private final ReleaseCycleRepository repository;
    private final DomainMapper mapper;

    public ReleaseCycleService(ReleaseCycleRepository repository, DomainMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ReleaseCycleDto> list(CycleStatus status, String sort) {
        Sort sortSpec = SortSupport.parse(sort, SORT_FIELDS, "targetVersionTag");
        return repository.findAll(sortSpec).stream()
                .filter(c -> status == null || c.getStatus() == status)
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReleaseCycleDto get(Long id) {
        return mapper.toDto(find(id));
    }

    public ReleaseCycleDto create(CreateCycleRequest request) {
        ReleaseCycle cycle = new ReleaseCycle();
        cycle.setTargetVersionTag(request.targetVersionTag());
        cycle.setPlannedDeploymentWindow(request.plannedDeploymentWindow());
        cycle.setStatus(CycleStatus.PLANNED);
        return mapper.toDto(repository.save(cycle));
    }

    public ReleaseCycleDto changeStatus(Long id, CycleStatus newStatus) {
        ReleaseCycle cycle = find(id);
        cycle.setStatus(newStatus);
        return mapper.toDto(cycle);
    }

    private ReleaseCycle find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono cyklu o id=" + id));
    }
}
