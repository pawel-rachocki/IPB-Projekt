package pl.pjatk.ipb.deployment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjatk.ipb.deployment.domain.ReleaseNotes;

public interface ReleaseNotesRepository extends JpaRepository<ReleaseNotes, Long> {
}
