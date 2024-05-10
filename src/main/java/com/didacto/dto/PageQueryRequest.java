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
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, defaultValue = "10")
    private int size = 10;
    @Schema(defaultValue = "createdTime")
    private String[] sort = {"createdTime"};
    @Schema(defaultValue = "DESC")
    private Sort.Direction direction = Sort.Direction.DESC;


    public PageRequest getPageable(){
        return PageRequest.of(page, size, Sort.by(sort));
    }
}