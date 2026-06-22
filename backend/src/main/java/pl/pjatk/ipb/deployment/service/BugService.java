package pl.pjatk.ipb.deployment.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjatk.ipb.deployment.domain.BugTicket;
import pl.pjatk.ipb.deployment.domain.ItsTicket;
import pl.pjatk.ipb.deployment.domain.enums.BugPriority;
import pl.pjatk.ipb.deployment.domain.enums.BugSource;
import pl.pjatk.ipb.deployment.domain.enums.BugStatus;
import pl.pjatk.ipb.deployment.repository.BugTicketRepository;
import pl.pjatk.ipb.deployment.repository.ItsTicketRepository;
import pl.pjatk.ipb.deployment.web.NotFoundException;
import pl.pjatk.ipb.deployment.web.dto.BugDto;
import pl.pjatk.ipb.deployment.web.dto.CreateBugRequest;
import pl.pjatk.ipb.deployment.web.dto.DomainMapper;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class BugService {

    private static final Set<String> SORT_FIELDS =
            Set.of("bugId", "priority", "status", "source");

    private final BugTicketRepository repository;
    private final ItsTicketRepository ticketRepository;
    private final DomainMapper mapper;

    public BugService(BugTicketRepository repository, ItsTicketRepository ticketRepository,
                      DomainMapper mapper) {
        this.repository = repository;
        this.ticketRepository = ticketRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<BugDto> list(BugStatus status, BugPriority priority, BugSource source, String sort) {
        Sort sortSpec = SortSupport.parse(sort, SORT_FIELDS, "bugId");
        return repository.findAll(sortSpec).stream()
                .filter(b -> status == null || b.getStatus() == status)
                .filter(b -> priority == null || b.getPriority() == priority)
                .filter(b -> source == null || b.getSource() == source)
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public BugDto get(String id) {
        return mapper.toDto(find(id));
    }

    public BugDto createForTicket(String ticketId, CreateBugRequest request) {
        if (repository.existsById(request.bugId())) {
            throw new IllegalArgumentException("Blad o id=" + request.bugId() + " juz istnieje");
        }
        ItsTicket parent = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono zadania o id=" + ticketId));
        BugTicket bug = new BugTicket();
        bug.setBugId(request.bugId());
        bug.setParentTicket(parent);
        bug.setDefectDescription(request.defectDescription());
        bug.setPriority(request.priority());
        bug.setSource(request.source());
        bug.setStatus(BugStatus.NEW);
        return mapper.toDto(repository.save(bug));
    }

    public BugDto changeStatus(String id, BugStatus newStatus) {
        BugTicket bug = find(id);
        bug.setStatus(newStatus);
        return mapper.toDto(bug);
    }

    private BugTicket find(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono bledu o id=" + id));
    }
}
