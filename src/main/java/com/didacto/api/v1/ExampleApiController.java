package com.didacto.api.v1;

import com.didacto.common.ExampleDefineCode;
import com.didacto.dto.example.ExampleRequestDto;
import com.didacto.dto.example.ExampleResponseDto;
import com.didacto.service.example.ExampleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//TODO : Swagger API Docs 설정 이후
// API 문서 설명 내용(API 용도, 요청 및 응답 모델의 필드 설명, 성공 및 실패 케이스 정의 등에 대한 문서 작성 코드를 추가한다.)

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/example")
public class ExampleApiController {

    private final ExampleService exampleService;

    @PostMapping()
    public ResponseEntity<Long> saveExample(
            @Valid @RequestBody ExampleRequestDto request
    ) {
        Long response = this.exampleService.addExample(request);

        //TODO : 공통 Response Model 정의하여 응답 형식을 통일시킨다.
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{keyword}")
    public ResponseEntity<List<ExampleResponseDto>> searchExampleByKey(
            @PathVariable String keyword
    ) {
        List<ExampleResponseDto> result = this.exampleService.searchExampleByKeyword(keyword);

        //TODO : 공통 Response Model 정의하여 응답 형식을 통일시킨다.
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
