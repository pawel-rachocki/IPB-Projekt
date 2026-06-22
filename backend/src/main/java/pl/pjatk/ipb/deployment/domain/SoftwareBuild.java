package pl.pjatk.ipb.deployment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "software_build")
@Getter
@Setter
@NoArgsConstructor
public class SoftwareBuild {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "build_id")
    private Long buildId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cycle_id", nullable = false)
    private ReleaseCycle cycle;

    @Column(name = "commit_hash", nullable = false, length = 40)
    private String commitHash;

    @Column(name = "compilation_date")
    private LocalDateTime compilationDate = LocalDateTime.now();

    @Column(name = "static_analysis_passed")
    private Boolean staticAnalysisPassed;
}
