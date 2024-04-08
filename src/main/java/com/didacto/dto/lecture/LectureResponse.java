package com.didacto.dto.lecture;

import com.didacto.domain.Lecture;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class LectureResponse {
    private Long id;
    private String title;
    private Long ownerId;
    private OffsetDateTime start_time;
    private OffsetDateTime end_time;
    private Boolean deleted;
    private OffsetDateTime modified_time;
    private OffsetDateTime created_time;

    public LectureResponse(Lecture lecture) {
        this.id = lecture.getId();
        this.title = lecture.getTitle();
        // TODO#23 : 도메인 변경에 따라 수정 필요
//        this.ownerId = lecture.getOwnerId();
        this.start_time = lecture.getStartTime();
        this.end_time = lecture.getEndTime();
        this.deleted = lecture.getDeleted();
        this.modified_time = lecture.getModifiedTime();
        this.created_time = lecture.getCreatedTime();
    }
}
