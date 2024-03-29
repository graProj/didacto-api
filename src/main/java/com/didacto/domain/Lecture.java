package com.didacto.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long id;

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

    @Column(nullable = false)
    private OffsetDateTime modifiedTime;

    @CreatedDate
    @Column(nullable = false)
    private OffsetDateTime createdTime;

    @Builder
    public Lecture(
            Long id,
            String title,
            Long ownerId,
            LectureState state,
            OffsetDateTime startTime,
            OffsetDateTime endTime,
            Boolean deleted,
            OffsetDateTime modifiedTime,
            OffsetDateTime createdTime
    ) {
        this.id = id;
        this.title = title;
        this.ownerId = ownerId;
        this.state = state;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deleted = deleted;
        this.modifiedTime = modifiedTime;
        this.createdTime = createdTime;
    }
}
