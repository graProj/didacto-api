package com.didacto.dto.lecturemember;

import com.didacto.domain.LectureMember;
import com.didacto.dto.PageQueryResponse;
import com.didacto.dto.enrollment.PageInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureMemberPageResponse extends PageQueryResponse {
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
