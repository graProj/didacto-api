package com.didacto.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "EXAMPLE")
@Data
public class Example {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXAM_ID")
    private Long examId;

    @Column(name = "NAME", nullable = false)
    private String name;
}
