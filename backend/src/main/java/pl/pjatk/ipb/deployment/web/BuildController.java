package pl.pjatk.ipb.deployment.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.pjatk.ipb.deployment.service.BuildService;
import pl.pjatk.ipb.deployment.web.dto.BuildDto;
import pl.pjatk.ipb.deployment.web.dto.CreateBuildRequest;

import java.util.List;

@RestController
@RequestMapping("/api/builds")
public class BuildController {

    private final BuildService service;

    public BuildController(BuildService service) {
        this.service = service;
    }

    @GetMapping
    public List<BuildDto> list(
            @RequestParam(required = false) Long cycleId,
            @RequestParam(required = false) Boolean staticAnalysisPassed,
            @RequestParam(required = false) String sort) {
        return service.list(cycleId, staticAnalysisPassed, sort);
    }

    @GetMapping("/{id}")
    public BuildDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BuildDto create(@Valid @RequestBody CreateBuildRequest request) {
        return service.create(request);
    }
}
