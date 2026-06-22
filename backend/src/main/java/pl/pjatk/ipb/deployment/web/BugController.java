package pl.pjatk.ipb.deployment.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.pjatk.ipb.deployment.domain.enums.BugPriority;
import pl.pjatk.ipb.deployment.domain.enums.BugSource;
import pl.pjatk.ipb.deployment.domain.enums.BugStatus;
import pl.pjatk.ipb.deployment.service.BugService;
import pl.pjatk.ipb.deployment.web.dto.BugDto;
import pl.pjatk.ipb.deployment.web.dto.CreateBugRequest;

import java.util.List;

@RestController
public class BugController {

    private final BugService service;

    public BugController(BugService service) {
        this.service = service;
    }

    @GetMapping("/api/bugs")
    public List<BugDto> list(
            @RequestParam(required = false) BugStatus status,
            @RequestParam(required = false) BugPriority priority,
            @RequestParam(required = false) BugSource source,
            @RequestParam(required = false) String sort) {
        return service.list(status, priority, source, sort);
    }

    @GetMapping("/api/bugs/{id}")
    public BugDto get(@PathVariable String id) {
        return service.get(id);
    }

    @PostMapping("/api/tickets/{ticketId}/bugs")
    @ResponseStatus(HttpStatus.CREATED)
    public BugDto create(@PathVariable String ticketId, @Valid @RequestBody CreateBugRequest request) {
        return service.createForTicket(ticketId, request);
    }

    @PatchMapping("/api/bugs/{id}/status")
    public BugDto changeStatus(@PathVariable String id, @RequestParam BugStatus value) {
        return service.changeStatus(id, value);
    }
}
