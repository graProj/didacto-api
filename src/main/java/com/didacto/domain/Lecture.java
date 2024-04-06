package com.didacto.domain;

import com.didacto.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Entity
public class Lecture extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long id;

    @Setter
    private String title;

    @Column(nullable = false)
    private Long ownerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LectureState state;

    private OffsetDateTime startTime;

    private OffsetDateTime endTime;

    @Column(nullable = false)
    private Boolean deleted;

    @OneToMany(mappedBy = "lecture")
    private List<Enrollment> enrollments;

    @Builder
    public Lecture(
            Long id,
            String title,
            Long ownerId,
            LectureState state,
            OffsetDateTime startTime,
            OffsetDateTime endTime,
            Boolean deleted
    ) {
        this.id = id;
        this.title = title;
        this.ownerId = ownerId;
        this.state = state;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deleted = deleted;
    }
}
