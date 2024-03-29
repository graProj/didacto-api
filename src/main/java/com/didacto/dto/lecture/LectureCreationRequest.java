package com.didacto.dto.lecture;

import lombok.Getter;

@Getter

public class LectureCreationRequest {
    private String title;
    private Long ownerId;
}
