package pl.pjatk.ipb.deployment.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjatk.ipb.deployment.domain.ItsTicket;
import pl.pjatk.ipb.deployment.domain.ReleaseCycle;
import pl.pjatk.ipb.deployment.domain.enums.TicketStatus;
import pl.pjatk.ipb.deployment.domain.enums.TicketType;
import pl.pjatk.ipb.deployment.repository.ItsTicketRepository;
import pl.pjatk.ipb.deployment.repository.ReleaseCycleRepository;
import pl.pjatk.ipb.deployment.web.NotFoundException;
import pl.pjatk.ipb.deployment.web.dto.CreateTicketRequest;
import pl.pjatk.ipb.deployment.web.dto.DomainMapper;
import pl.pjatk.ipb.deployment.web.dto.TicketDto;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class TicketService {

    private static final Set<String> SORT_FIELDS =
            Set.of("ticketId", "title", "status", "type");

    private final ItsTicketRepository repository;
    private final ReleaseCycleRepository cycleRepository;
    private final DomainMapper mapper;

    public TicketService(ItsTicketRepository repository, ReleaseCycleRepository cycleRepository,
                         DomainMapper mapper) {
        this.repository = repository;
        this.cycleRepository = cycleRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<TicketDto> list(TicketStatus status, TicketType type, Long cycleId, String sort) {
        Sort sortSpec = SortSupport.parse(sort, SORT_FIELDS, "ticketId");
        return repository.findAll(sortSpec).stream()
                .filter(t -> status == null || t.getStatus() == status)
                .filter(t -> type == null || t.getType() == type)
                .filter(t -> cycleId == null
                        || (t.getCycle() != null && cycleId.equals(t.getCycle().getCycleId())))
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TicketDto get(String id) {
        return mapper.toDto(find(id));
    }

    public TicketDto create(CreateTicketRequest request) {
        if (repository.existsById(request.ticketId())) {
            throw new IllegalArgumentException("Zadanie o id=" + request.ticketId() + " juz istnieje");
        }
        ItsTicket ticket = new ItsTicket();
        ticket.setTicketId(request.ticketId());
        ticket.setTitle(request.title());
        ticket.setRequirementDescription(request.requirementDescription());
        ticket.setType(request.type());
        ticket.setStatus(TicketStatus.TO_DO);
        ticket.setAssigneeId(request.assigneeId());
        if (request.cycleId() != null) {
            ticket.setCycle(findCycle(request.cycleId()));
        }
        return mapper.toDto(repository.save(ticket));
    }

    public TicketDto changeStatus(String id, TicketStatus newStatus) {
        ItsTicket ticket = find(id);
        ticket.setStatus(newStatus);
        return mapper.toDto(ticket);
    }

    private ItsTicket find(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono zadania o id=" + id));
    }

    private ReleaseCycle findCycle(Long cycleId) {
        return cycleRepository.findById(cycleId)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono cyklu o id=" + cycleId));
    }
}
