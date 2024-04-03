package com.didacto.domain;

import com.didacto.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Entity
public class Enrollment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long id;

    @Column(nullable = false)
    private Long lectureId;

    @Column(nullable = false)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status = EnrollmentStatus.WAITING;

    private Long modified_by;
}
