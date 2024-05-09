package com.didacto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQueryRequest {
    @Schema(required = true, example = "1", minimum = "1")
    private int page;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "10", description = "Default : 10")
    private int size = 10;
    @Schema(example = "createdTime", description = "Default : createdTime")
    private String[] sort = {"createdTime"};
    @Schema(example = "DESC", description = "Default : DESC")
    private Sort.Direction direction = Sort.Direction.DESC;


    public PageRequest toPageable(){
        return PageRequest.of(page, size, Sort.by(sort));
    }
}