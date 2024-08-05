package com.didacto.domain;

import com.didacto.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;


@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseEntity {
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

    private Boolean deleted;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    /**
     * 연관관계 세팅
     */

    @OneToMany(mappedBy = "owner")
    private List<Lecture> ownLectures;

    @OneToMany(mappedBy = "member")
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "member")
    private List<LectureMember> lectureMembers;

    @Builder
    public Member(Long id, String email, String password, String name, Authority role, OffsetDateTime birth, Boolean deleted, Grade grade) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.birth = birth;
        this.deleted = false;
        this.grade = Grade.Freeteer;
    }

    public void modify(String password,String name, OffsetDateTime birth) {
        this.password = password;
        this.name = name;
        this.birth = birth;
    }

    public void delete() {
        this.deleted = true;
    }

    public void premium() {this.grade = Grade.Premium;}
}
