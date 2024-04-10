package com.didacto.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;


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

    private Boolean deleted;

    /**
     * 생성일, 수정일 값 세팅
     */
    @PrePersist
    public void prePersist(){
        this.created_date = OffsetDateTime.now();
    }


    @Builder
    public Member(Long id, String email, String password, String name, Authority role, OffsetDateTime birth, Boolean deleted) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.birth = birth;
        this.deleted = false;
    }

    public void modify(String password,String name, OffsetDateTime birth) {
        this.password = password;
        this.name = name;
        this.birth = birth;
    }

    public void delete() {
        this.deleted = true;
    }
}