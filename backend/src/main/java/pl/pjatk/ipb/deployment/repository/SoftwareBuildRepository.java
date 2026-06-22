package pl.pjatk.ipb.deployment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjatk.ipb.deployment.domain.SoftwareBuild;

public interface SoftwareBuildRepository extends JpaRepository<SoftwareBuild, Long> {
}
