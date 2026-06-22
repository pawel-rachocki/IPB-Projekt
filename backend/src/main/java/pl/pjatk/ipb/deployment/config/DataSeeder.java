package pl.pjatk.ipb.deployment.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.pjatk.ipb.deployment.domain.BugTicket;
import pl.pjatk.ipb.deployment.domain.ItsTicket;
import pl.pjatk.ipb.deployment.domain.PortalDeployment;
import pl.pjatk.ipb.deployment.domain.ReleaseCycle;
import pl.pjatk.ipb.deployment.domain.ReleaseNotes;
import pl.pjatk.ipb.deployment.domain.SoftwareBuild;
import pl.pjatk.ipb.deployment.domain.UatVerification;
import pl.pjatk.ipb.deployment.domain.enums.BugPriority;
import pl.pjatk.ipb.deployment.domain.enums.BugSource;
import pl.pjatk.ipb.deployment.domain.enums.BugStatus;
import pl.pjatk.ipb.deployment.domain.enums.CycleStatus;
import pl.pjatk.ipb.deployment.domain.enums.DeploymentStatus;
import pl.pjatk.ipb.deployment.domain.enums.RnViewType;
import pl.pjatk.ipb.deployment.domain.enums.TicketStatus;
import pl.pjatk.ipb.deployment.domain.enums.TicketType;
import pl.pjatk.ipb.deployment.domain.enums.UatStatus;
import pl.pjatk.ipb.deployment.repository.BugTicketRepository;
import pl.pjatk.ipb.deployment.repository.ItsTicketRepository;
import pl.pjatk.ipb.deployment.repository.PortalDeploymentRepository;
import pl.pjatk.ipb.deployment.repository.ReleaseCycleRepository;
import pl.pjatk.ipb.deployment.repository.ReleaseNotesRepository;
import pl.pjatk.ipb.deployment.repository.SoftwareBuildRepository;
import pl.pjatk.ipb.deployment.repository.UatVerificationRepository;

import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ReleaseCycleRepository cycleRepo;
    private final ItsTicketRepository ticketRepo;
    private final BugTicketRepository bugRepo;
    private final SoftwareBuildRepository buildRepo;
    private final ReleaseNotesRepository notesRepo;
    private final PortalDeploymentRepository deploymentRepo;
    private final UatVerificationRepository verificationRepo;

    public DataSeeder(ReleaseCycleRepository cycleRepo,
                      ItsTicketRepository ticketRepo,
                      BugTicketRepository bugRepo,
                      SoftwareBuildRepository buildRepo,
                      ReleaseNotesRepository notesRepo,
                      PortalDeploymentRepository deploymentRepo,
                      UatVerificationRepository verificationRepo) {
        this.cycleRepo = cycleRepo;
        this.ticketRepo = ticketRepo;
        this.bugRepo = bugRepo;
        this.buildRepo = buildRepo;
        this.notesRepo = notesRepo;
        this.deploymentRepo = deploymentRepo;
        this.verificationRepo = verificationRepo;
    }

    @Override
    public void run(String... args) {
        if (cycleRepo.count() > 0) {
            return;
        }

        ReleaseCycle c1 = cycle("v1.4.0", CycleStatus.DEPLOYED, LocalDateTime.now().minusDays(30));
        ReleaseCycle c2 = cycle("v1.5.0", CycleStatus.QA_TESTING, LocalDateTime.now().plusDays(7));
        ReleaseCycle c3 = cycle("v2.0.0", CycleStatus.PLANNED, LocalDateTime.now().plusDays(45));

        ItsTicket t1 = ticket("ITS-001", c2, "Logowanie przez SSO",
                TicketType.NEW_FEATURE, TicketStatus.DONE, 101L);
        ItsTicket t2 = ticket("ITS-002", c2, "Optymalizacja zapytan raportu",
                TicketType.ENHANCEMENT, TicketStatus.IN_PROGRESS, 102L);
        ItsTicket t3 = ticket("ITS-003", c2, "Eksport zestawienia do PDF",
                TicketType.NEW_FEATURE, TicketStatus.QA_FAILED, 101L);
        ItsTicket t4 = ticket("ITS-004", c3, "Powiadomienia push",
                TicketType.NEW_FEATURE, TicketStatus.TO_DO, null);
        ticket("ITS-005", c3, "Tryb ciemny interfejsu",
                TicketType.ENHANCEMENT, TicketStatus.TO_DO, null);
        ItsTicket t6 = ticket("ITS-006", c1, "Integracja z bramka platnosci",
                TicketType.NEW_FEATURE, TicketStatus.DONE, 103L);
        ticket("ITS-007", c2, "Walidacja formularzy rejestracji",
                TicketType.ENHANCEMENT, TicketStatus.CODE_REVIEW, 102L);

        bug("BUG-001", t3, "Eksport ucina ostatnia strone raportu",
                BugPriority.HIGH, BugSource.SQA, BugStatus.NEW);
        bug("BUG-002", t2, "Problem N+1 w zapytaniu o pozycje raportu",
                BugPriority.MEDIUM, BugSource.CODE_REVIEW, BugStatus.IN_PROGRESS);
        bug("BUG-003", t1, "Nieuzywany import wykryty statycznie",
                BugPriority.LOW, BugSource.STATIC_ANALYSIS, BugStatus.VERIFIED);
        bug("BUG-004", t6, "Podwojne obciazenie karty przy ponowieniu",
                BugPriority.CRITICAL, BugSource.UAT, BugStatus.VERIFIED);
        bug("BUG-005", t3, "Brak walidacji rozmiaru pliku eksportu",
                BugPriority.CRITICAL, BugSource.SQA, BugStatus.NEW);

        SoftwareBuild b1 = build(c2, "a1b2c3d4", true);
        build(c2, "d4e5f6a7", false);
        SoftwareBuild b3 = build(c1, "99887766", true);
        build(c3, "11223344", true);

        notes(b1, RnViewType.INTERNAL, "Wewnetrzne: SSO, refaktor warstwy raportow.");
        notes(b1, RnViewType.UAT, "Dla UAT: nowe logowanie SSO oraz szybsze raporty.");
        notes(b3, RnViewType.PRODUCTION, "Produkcja v1.4.0: integracja platnosci, poprawki stabilnosci.");

        PortalDeployment d1 = deployment(c1, "https://uat.portal.example/v1.4.0",
                UatStatus.ACCEPTED, DeploymentStatus.SUCCESS, LocalDateTime.now().minusDays(20));
        PortalDeployment d2 = deployment(c2, "https://uat.portal.example/v1.5.0",
                UatStatus.IN_PROGRESS, DeploymentStatus.PLANNED, null);

        verification(d1, true, true, true, null);
        verification(d2, false, true, true, "Niespelnione wymaganie: brak eksportu do PDF");
    }

    private ReleaseCycle cycle(String tag, CycleStatus status, LocalDateTime window) {
        ReleaseCycle c = new ReleaseCycle();
        c.setTargetVersionTag(tag);
        c.setStatus(status);
        c.setPlannedDeploymentWindow(window);
        return cycleRepo.save(c);
    }

    private ItsTicket ticket(String id, ReleaseCycle cycle, String title,
                             TicketType type, TicketStatus status, Long assignee) {
        ItsTicket t = new ItsTicket();
        t.setTicketId(id);
        t.setCycle(cycle);
        t.setTitle(title);
        t.setRequirementDescription("Wymaganie dla: " + title);
        t.setType(type);
        t.setStatus(status);
        t.setAssigneeId(assignee);
        return ticketRepo.save(t);
    }

    private BugTicket bug(String id, ItsTicket parent, String description,
                          BugPriority priority, BugSource source, BugStatus status) {
        BugTicket b = new BugTicket();
        b.setBugId(id);
        b.setParentTicket(parent);
        b.setDefectDescription(description);
        b.setPriority(priority);
        b.setSource(source);
        b.setStatus(status);
        return bugRepo.save(b);
    }

    private SoftwareBuild build(ReleaseCycle cycle, String commitHash, boolean staticAnalysisPassed) {
        SoftwareBuild b = new SoftwareBuild();
        b.setCycle(cycle);
        b.setCommitHash(commitHash);
        b.setStaticAnalysisPassed(staticAnalysisPassed);
        return buildRepo.save(b);
    }

    private ReleaseNotes notes(SoftwareBuild build, RnViewType viewType, String content) {
        ReleaseNotes rn = new ReleaseNotes();
        rn.setBuild(build);
        rn.setViewType(viewType);
        rn.setTextContent(content);
        return notesRepo.save(rn);
    }

    private PortalDeployment deployment(ReleaseCycle cycle, String url, UatStatus uatStatus,
                                        DeploymentStatus deploymentStatus, LocalDateTime approvedProdDate) {
        PortalDeployment d = new PortalDeployment();
        d.setCycle(cycle);
        d.setUatEnvironmentUrl(url);
        d.setUatStatus(uatStatus);
        d.setDeploymentStatus(deploymentStatus);
        d.setApprovedProdDate(approvedProdDate);
        return deploymentRepo.save(d);
    }

    private UatVerification verification(PortalDeployment deployment, boolean requirementsMet,
                                         boolean noOperationalImpact, boolean securityAuditPassed,
                                         String rejectionJustification) {
        UatVerification v = new UatVerification();
        v.setDeployment(deployment);
        v.setRequirementsMet(requirementsMet);
        v.setNoOperationalImpact(noOperationalImpact);
        v.setSecurityAuditPassed(securityAuditPassed);
        v.setRejectionJustification(rejectionJustification);
        return verificationRepo.save(v);
    }
}
