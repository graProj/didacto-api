package com.didacto.dto.lecturemember;

import com.didacto.domain.LectureMember;
import com.didacto.dto.lecture.LectureResponse;
import com.didacto.dto.member.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class LectureMemberResponse {
    private Long id;
    private LectureResponse lecture;
    private MemberResponse member;
    private Boolean deleted;
    private MemberResponse modifiedBy;
    private OffsetDateTime modifiedTime;

    public LectureMemberResponse(LectureMember lectureMember){
        this.id = lectureMember.getId();
        this.lecture = new LectureResponse(lectureMember.getLecture());
        this.member = new MemberResponse(lectureMember.getMember());
        this.deleted = lectureMember.getDeleted();
        this.modifiedBy = new MemberResponse(lectureMember.getModifiedBy());
        this.modifiedTime = lectureMember.getModifiedTime();
    }
}
