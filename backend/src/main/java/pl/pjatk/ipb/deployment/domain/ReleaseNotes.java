package pl.pjatk.ipb.deployment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pjatk.ipb.deployment.domain.enums.RnViewType;

import java.time.LocalDateTime;

@Entity
@Table(name = "release_notes")
@Getter
@Setter
@NoArgsConstructor
public class ReleaseNotes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rn_id")
    private Long rnId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "build_id", nullable = false)
    private SoftwareBuild build;

    @Enumerated(EnumType.STRING)
    @Column(name = "view_type_code", nullable = false, length = 50)
    private RnViewType viewType;

    @Lob
    @Column(name = "text_content")
    private String textContent;

    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate = LocalDateTime.now();
}
