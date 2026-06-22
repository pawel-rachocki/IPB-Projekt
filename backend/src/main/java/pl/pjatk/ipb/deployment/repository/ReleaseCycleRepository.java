package pl.pjatk.ipb.deployment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjatk.ipb.deployment.domain.ReleaseCycle;

public interface ReleaseCycleRepository extends JpaRepository<ReleaseCycle, Long> {
}
