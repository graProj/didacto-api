package com.didacto.dto.lecture;

import com.didacto.dto.PageQueryRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureQueryRequest extends PageQueryRequest {
    private String titleKeyword;
    private Boolean deleted;
}
