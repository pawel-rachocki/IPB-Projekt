package pl.pjatk.ipb.deployment.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.pjatk.ipb.deployment.domain.enums.RnViewType;
import pl.pjatk.ipb.deployment.service.ReleaseNotesService;
import pl.pjatk.ipb.deployment.web.dto.CreateReleaseNotesRequest;
import pl.pjatk.ipb.deployment.web.dto.ReleaseNotesDto;
import pl.pjatk.ipb.deployment.web.dto.UpdateReleaseNotesRequest;

import java.util.List;

@RestController
@RequestMapping("/api/release-notes")
public class ReleaseNotesController {

    private final ReleaseNotesService service;

    public ReleaseNotesController(ReleaseNotesService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReleaseNotesDto> list(
            @RequestParam(required = false) RnViewType viewType,
            @RequestParam(required = false) Long buildId,
            @RequestParam(required = false) String sort) {
        return service.list(viewType, buildId, sort);
    }

    @GetMapping("/{id}")
    public ReleaseNotesDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReleaseNotesDto create(@Valid @RequestBody CreateReleaseNotesRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ReleaseNotesDto update(@PathVariable Long id, @Valid @RequestBody UpdateReleaseNotesRequest request) {
        return service.update(id, request);
    }
}
