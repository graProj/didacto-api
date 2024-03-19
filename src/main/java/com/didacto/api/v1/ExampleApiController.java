package com.didacto.api.v1;

import com.didacto.common.ExampleDefineCode;
import com.didacto.dto.example.ExampleRequestDto;
import com.didacto.dto.example.ExampleResponseDto;
import com.didacto.service.example.ExampleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//TODO : Swagger API Docs 설정 이후
// API 문서 설명 내용(API 용도, 요청 및 응답 모델의 필드 설명, 성공 및 실패 케이스 정의 등에 대한 문서 작성 코드를 추가한다.)


@Tag(name = "EXAM API", description = "예제와 관련된 API") // Swagger Docs : API 이름
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/example")
public class ExampleApiController {

    private final ExampleService exampleService;

    @PostMapping()
    @Operation(summary = "EXAM_01 : 저장", description = "Example을 저장시킨다.")   // Swagger API 기능 설명
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success") // Swagger API : 응답 케이스 설명
    })
    public ResponseEntity<Long> saveExample(
            @Valid @RequestBody ExampleRequestDto request
    ) {
        Long response = this.exampleService.addExample(request);

        //TODO : 공통 Response Model 정의하여 응답 형식을 통일시킨다.
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{pathValue}")
    @Operation(summary = "EXAM_02 : 키워드 조회", description = "키워드가 포함된 Example 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success") // Swagger API : 응답 케이스 설명
    })
    public ResponseEntity<List<ExampleResponseDto>> searchExampleByKey(
            // Path Variable 사용 예시 : /api/v1/example/blah
            @PathVariable
            @Schema(description = "Path Variable 예시", example = "blah")   //Swagger 파라미터 설명
            String pathValue,

            // Query Parameter 사용 예시 : /api/v1/example?paramKeyword=blah
            @RequestParam
            @Parameter(name = "paramKeyword", description = "Parameter 예시",
                    example = "blah", required = true) //Swagger 파라미터 설명
            String paramValue

    ) {
        List<ExampleResponseDto> result = this.exampleService.searchExampleByKeyword(pathValue);

        //TODO : 공통 Response Model 정의하여 응답 형식을 통일시킨다.
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
