package com.didacto.dto.lecturemember;

import com.didacto.domain.LectureMember;
import com.didacto.dto.PageResponse;
import com.didacto.dto.PageInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureMemberPageResponse extends PageResponse {
    private List<LectureMemberResponse> lectureMembers;

    public LectureMemberPageResponse(
            PageInfoResponse pageInfo,
            List<LectureMember> lectureMembers
    ){
        super(pageInfo);
        this.lectureMembers = lectureMembers.stream()
                .map(LectureMemberResponse::new)
                .toList();
    }
}
