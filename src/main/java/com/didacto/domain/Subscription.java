package com.didacto.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;

    @Builder
    public Subscription(Member member, OffsetDateTime startDate, OffsetDateTime endDate) {
        this.member = member;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
