package com.didacto.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

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
    private Date birth;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(nullable = false)
    private Timestamp created_date;

    @Builder
    public Member(Long id, String email, String password, String name, Date birth, Authority authority, Timestamp created_date) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.authority = authority;
        this.created_date = created_date;
    }
}