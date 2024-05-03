package com.didacto.dto.lecture;

import com.didacto.dto.enrollment.PageInfoResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "Lecutre : 데이터 리스트 응답")
public class LectureListResponse {

    private List<LectureResponse> lectures;

    private PageInfoResponse pageInfo;
}
