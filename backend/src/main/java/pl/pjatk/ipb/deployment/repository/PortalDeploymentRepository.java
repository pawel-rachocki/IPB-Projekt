package pl.pjatk.ipb.deployment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjatk.ipb.deployment.domain.PortalDeployment;

public interface PortalDeploymentRepository extends JpaRepository<PortalDeployment, Long> {
}
