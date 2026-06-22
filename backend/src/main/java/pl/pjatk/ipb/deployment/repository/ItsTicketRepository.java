package pl.pjatk.ipb.deployment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjatk.ipb.deployment.domain.ItsTicket;

public interface ItsTicketRepository extends JpaRepository<ItsTicket, String> {
}
