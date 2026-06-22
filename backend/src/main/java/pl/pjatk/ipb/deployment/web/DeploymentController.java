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
import pl.pjatk.ipb.deployment.domain.enums.DeploymentStatus;
import pl.pjatk.ipb.deployment.domain.enums.UatStatus;
import pl.pjatk.ipb.deployment.service.DeploymentService;
import pl.pjatk.ipb.deployment.web.dto.CreateDeploymentRequest;
import pl.pjatk.ipb.deployment.web.dto.DeploymentDto;

import java.util.List;

@RestController
@RequestMapping("/api/deployments")
public class DeploymentController {

    private final DeploymentService service;

    public DeploymentController(DeploymentService service) {
        this.service = service;
    }

    @GetMapping
    public List<DeploymentDto> list(
            @RequestParam(required = false) DeploymentStatus deploymentStatus,
            @RequestParam(required = false) UatStatus uatStatus,
            @RequestParam(required = false) String sort) {
        return service.list(deploymentStatus, uatStatus, sort);
    }

    @GetMapping("/{id}")
    public DeploymentDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeploymentDto create(@Valid @RequestBody CreateDeploymentRequest request) {
        return service.create(request);
    }

    @PatchMapping("/{id}/uat-status")
    public DeploymentDto changeUatStatus(@PathVariable Long id, @RequestParam UatStatus value) {
        return service.changeUatStatus(id, value);
    }

    @PatchMapping("/{id}/deployment-status")
    public DeploymentDto changeDeploymentStatus(@PathVariable Long id, @RequestParam DeploymentStatus value) {
        return service.changeDeploymentStatus(id, value);
    }
}
