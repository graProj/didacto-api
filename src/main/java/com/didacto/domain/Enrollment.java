package com.didacto.domain;

import com.didacto.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Enrollment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status = EnrollmentStatus.WAITING;


    /**
     * 연관관꼐 매핑
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by", nullable = false)
    private Member modified_by;


    public void updateStatus(EnrollmentStatus status){
        this.status = status;
    }

    public void updateModifiedMember(Member modifiedMember){
        this.modified_by = modifiedMember;
    }


}
