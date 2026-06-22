package pl.pjatk.ipb.deployment.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjatk.ipb.deployment.domain.ReleaseCycle;
import pl.pjatk.ipb.deployment.domain.SoftwareBuild;
import pl.pjatk.ipb.deployment.repository.ReleaseCycleRepository;
import pl.pjatk.ipb.deployment.repository.SoftwareBuildRepository;
import pl.pjatk.ipb.deployment.web.NotFoundException;
import pl.pjatk.ipb.deployment.web.dto.BuildDto;
import pl.pjatk.ipb.deployment.web.dto.CreateBuildRequest;
import pl.pjatk.ipb.deployment.web.dto.DomainMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class BuildService {

    private static final Set<String> SORT_FIELDS =
            Set.of("buildId", "commitHash", "compilationDate");

    private final SoftwareBuildRepository repository;
    private final ReleaseCycleRepository cycleRepository;
    private final DomainMapper mapper;

    public BuildService(SoftwareBuildRepository repository, ReleaseCycleRepository cycleRepository,
                        DomainMapper mapper) {
        this.repository = repository;
        this.cycleRepository = cycleRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<BuildDto> list(Long cycleId, Boolean staticAnalysisPassed, String sort) {
        Sort sortSpec = SortSupport.parse(sort, SORT_FIELDS, "compilationDate");
        return repository.findAll(sortSpec).stream()
                .filter(b -> cycleId == null
                        || (b.getCycle() != null && cycleId.equals(b.getCycle().getCycleId())))
                .filter(b -> staticAnalysisPassed == null
                        || staticAnalysisPassed.equals(b.getStaticAnalysisPassed()))
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public BuildDto get(Long id) {
        return mapper.toDto(find(id));
    }

    public BuildDto create(CreateBuildRequest request) {
        ReleaseCycle cycle = cycleRepository.findById(request.cycleId())
                .orElseThrow(() -> new NotFoundException("Nie znaleziono cyklu o id=" + request.cycleId()));
        SoftwareBuild build = new SoftwareBuild();
        build.setCycle(cycle);
        build.setCommitHash(request.commitHash());
        build.setCompilationDate(LocalDateTime.now());
        build.setStaticAnalysisPassed(request.staticAnalysisPassed());
        return mapper.toDto(repository.save(build));
    }

    private SoftwareBuild find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono buildu o id=" + id));
    }
}
