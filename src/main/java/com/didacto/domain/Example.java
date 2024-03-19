package com.didacto.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EXAMPLE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Example {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXAM_ID")
    private Long examId;

    @Column(name = "NAME", nullable = false)
    private String name;
}
