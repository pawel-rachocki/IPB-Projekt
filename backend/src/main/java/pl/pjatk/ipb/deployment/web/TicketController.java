package pl.pjatk.ipb.deployment.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.pjatk.ipb.deployment.domain.enums.TicketStatus;
import pl.pjatk.ipb.deployment.domain.enums.TicketType;
import pl.pjatk.ipb.deployment.service.TicketService;
import pl.pjatk.ipb.deployment.web.dto.CreateTicketRequest;
import pl.pjatk.ipb.deployment.web.dto.TicketDto;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @GetMapping
    public List<TicketDto> list(
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) TicketType type,
            @RequestParam(required = false) Long cycleId,
            @RequestParam(required = false) String sort) {
        return service.list(status, type, cycleId, sort);
    }

    @GetMapping("/{id}")
    public TicketDto get(@PathVariable String id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketDto create(@Valid @RequestBody CreateTicketRequest request) {
        return service.create(request);
    }

    @PatchMapping("/{id}/status")
    public TicketDto changeStatus(@PathVariable String id, @RequestParam TicketStatus value) {
        return service.changeStatus(id, value);
    }
}
