package pl.pjatk.ipb.deployment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjatk.ipb.deployment.domain.BugTicket;

public interface BugTicketRepository extends JpaRepository<BugTicket, String> {
}
