package com.didacto.dto.lecturemember;

import com.didacto.dto.PageQueryRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureMemberQueryRequest extends PageQueryRequest {
    private Boolean deleted;
}
