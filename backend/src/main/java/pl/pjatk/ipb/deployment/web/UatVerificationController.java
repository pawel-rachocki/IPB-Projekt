package pl.pjatk.ipb.deployment.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.pjatk.ipb.deployment.service.UatVerificationService;
import pl.pjatk.ipb.deployment.web.dto.CreateVerificationRequest;
import pl.pjatk.ipb.deployment.web.dto.UatVerificationDto;

import java.util.List;

@RestController
public class UatVerificationController {

    private final UatVerificationService service;

    public UatVerificationController(UatVerificationService service) {
        this.service = service;
    }

    @GetMapping("/api/uat-verifications")
    public List<UatVerificationDto> list(
            @RequestParam(required = false) Long deploymentId,
            @RequestParam(required = false) String sort) {
        return service.list(deploymentId, sort);
    }

    @GetMapping("/api/uat-verifications/{id}")
    public UatVerificationDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping("/api/deployments/{deploymentId}/verifications")
    @ResponseStatus(HttpStatus.CREATED)
    public UatVerificationDto create(@PathVariable Long deploymentId,
                                     @Valid @RequestBody CreateVerificationRequest request) {
        return service.createForDeployment(deploymentId, request);
    }
}
