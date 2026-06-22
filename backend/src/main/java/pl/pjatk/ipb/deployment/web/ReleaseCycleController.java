package pl.pjatk.ipb.deployment.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.pjatk.ipb.deployment.domain.enums.CycleStatus;
import pl.pjatk.ipb.deployment.service.ReleaseCycleService;
import pl.pjatk.ipb.deployment.web.dto.CreateCycleRequest;
import pl.pjatk.ipb.deployment.web.dto.ReleaseCycleDto;

import java.util.List;

@RestController
@RequestMapping("/api/release-cycles")
public class ReleaseCycleController {

    private final ReleaseCycleService service;

    public ReleaseCycleController(ReleaseCycleService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReleaseCycleDto> list(
            @RequestParam(required = false) CycleStatus status,
            @RequestParam(required = false) String sort) {
        return service.list(status, sort);
    }

    @GetMapping("/{id}")
    public ReleaseCycleDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReleaseCycleDto create(@Valid @RequestBody CreateCycleRequest request) {
        return service.create(request);
    }

    @PatchMapping("/{id}/status")
    public ReleaseCycleDto changeStatus(@PathVariable Long id, @RequestParam CycleStatus value) {
        return service.changeStatus(id, value);
    }
}
