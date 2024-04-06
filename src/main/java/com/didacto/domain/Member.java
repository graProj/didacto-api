package com.didacto.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.List;


@Getter
@NoArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column
    private OffsetDateTime birth;

    @Enumerated(EnumType.STRING)
    private Authority role;

    @CreatedDate
    @Column(nullable = false)
    private OffsetDateTime created_date;

    @OneToMany(mappedBy = "member_id")
    private List<Enrollment> enrollments;


    /**
     * 생성일, 수정일 값 세팅
     */
    @PrePersist
    public void prePersist(){
        this.created_date = OffsetDateTime.now();
    }


    @Builder
    public Member(Long id, String email, String password, String name, Authority role, OffsetDateTime birth) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.birth = birth;
    }
}