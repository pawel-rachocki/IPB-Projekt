package pl.pjatk.ipb.deployment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjatk.ipb.deployment.domain.UatVerification;

public interface UatVerificationRepository extends JpaRepository<UatVerification, Long> {
}
