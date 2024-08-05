package com.didacto.domain;

import com.didacto.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.transaction.annotation.Transactional;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LectureState state;

    private OffsetDateTime startTime;

    private OffsetDateTime endTime;

    @Column(nullable = false)
    private Boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Member owner;

    @OneToMany(mappedBy = "lecture")
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "lecture")
    private List<LectureMember> lectureMembers;

    @Builder
    public Lecture(
            Long id,
            String title,
            LectureState state,
            OffsetDateTime startTime,
            OffsetDateTime endTime,
            Member owner,
            List<Enrollment> enrollments,
            List<LectureMember> lectureMembers
    ) {
        this.id = id;
        this.title = title;
        this.state = state;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deleted = false;
        this.owner = owner;
        this.enrollments = enrollments;
        this.lectureMembers = lectureMembers;
    }

    public void modify(String title) {
        this.title = title;
    }
    
    public void delete() {
        this.deleted = true;
    }
}
