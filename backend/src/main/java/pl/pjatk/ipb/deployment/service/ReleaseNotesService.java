package pl.pjatk.ipb.deployment.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjatk.ipb.deployment.domain.ReleaseNotes;
import pl.pjatk.ipb.deployment.domain.SoftwareBuild;
import pl.pjatk.ipb.deployment.domain.enums.RnViewType;
import pl.pjatk.ipb.deployment.repository.ReleaseNotesRepository;
import pl.pjatk.ipb.deployment.repository.SoftwareBuildRepository;
import pl.pjatk.ipb.deployment.web.NotFoundException;
import pl.pjatk.ipb.deployment.web.dto.CreateReleaseNotesRequest;
import pl.pjatk.ipb.deployment.web.dto.DomainMapper;
import pl.pjatk.ipb.deployment.web.dto.ReleaseNotesDto;
import pl.pjatk.ipb.deployment.web.dto.UpdateReleaseNotesRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ReleaseNotesService {

    private static final Set<String> SORT_FIELDS =
            Set.of("rnId", "viewType", "lastUpdatedDate");

    private final ReleaseNotesRepository repository;
    private final SoftwareBuildRepository buildRepository;
    private final DomainMapper mapper;

    public ReleaseNotesService(ReleaseNotesRepository repository, SoftwareBuildRepository buildRepository,
                               DomainMapper mapper) {
        this.repository = repository;
        this.buildRepository = buildRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ReleaseNotesDto> list(RnViewType viewType, Long buildId, String sort) {
        Sort sortSpec = SortSupport.parse(sort, SORT_FIELDS, "lastUpdatedDate");
        return repository.findAll(sortSpec).stream()
                .filter(rn -> viewType == null || rn.getViewType() == viewType)
                .filter(rn -> buildId == null
                        || (rn.getBuild() != null && buildId.equals(rn.getBuild().getBuildId())))
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReleaseNotesDto get(Long id) {
        return mapper.toDto(find(id));
    }

    public ReleaseNotesDto create(CreateReleaseNotesRequest request) {
        SoftwareBuild build = buildRepository.findById(request.buildId())
                .orElseThrow(() -> new NotFoundException("Nie znaleziono buildu o id=" + request.buildId()));
        ReleaseNotes notes = new ReleaseNotes();
        notes.setBuild(build);
        notes.setViewType(request.viewType());
        notes.setTextContent(request.textContent());
        notes.setLastUpdatedDate(LocalDateTime.now());
        return mapper.toDto(repository.save(notes));
    }

    public ReleaseNotesDto update(Long id, UpdateReleaseNotesRequest request) {
        ReleaseNotes notes = find(id);
        notes.setViewType(request.viewType());
        notes.setTextContent(request.textContent());
        notes.setLastUpdatedDate(LocalDateTime.now());
        return mapper.toDto(notes);
    }

    private ReleaseNotes find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono Release Notes o id=" + id));
    }
}
