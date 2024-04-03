package com.didacto.api.v1;

import com.didacto.common.ErrorDefineCode;
import com.didacto.common.response.CommonResponse;
import com.didacto.common.response.SwaggerErrorResponseType;
import com.didacto.config.exception.custom.exception.ForbiddenException403;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.config.exception.custom.exception.UnsupportedMediaTypeException415;
import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.dto.example.ExampleRequest;
import com.didacto.dto.example.ExampleResponse;
import com.didacto.dto.example.ExampleValidationRequest;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Tag(name = "EXAM API", description = "예제와 관련된 API") // Swagger Docs : API 이름
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/example")
public class ExampleApiController {

    private final ExampleService exampleService;


    @PostMapping()
    @Operation(summary = "EXAM_API_01 : 저장", description = "Example을 저장시킨다.")   // Swagger API 기능 설명
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "중복된 이름의 Example", // Swagger API : 응답 케이스 설명
                    content = {@Content(schema = @Schema(implementation = SwaggerErrorResponseType.class))})
    })
    public CommonResponse<Long> saveExample(
            @Valid @RequestBody ExampleRequest request
    ) {
        Long result = this.exampleService.addExample(request);

        return new CommonResponse(true, HttpStatus.OK, "Example 저장에 성공했습니다", result);
    }

    @GetMapping("/{pathValue}")
    @Operation(summary = "EXAM_API_02 : 키워드 조회", description = "키워드가 포함된 Example 리스트를 조회한다.")
    @ApiResponses(value = {
    })
    public CommonResponse<List<ExampleResponse>> queryExampleByKey(
            // Path Variable 사용 예시 : /api/v1/example/blah
            @Schema(description = "Path Variable 예시", example = "blah")   //Swagger 파라미터 설명
            @PathVariable String pathValue,

            // Query Parameter 사용 예시 : /api/v1/example?paramKeyword=blah
            @Parameter(description = "Parameter 예시", example = "blah") //Swagger 파라미터 설명
            @RequestParam(required = false) String paramValue

    ) {
        List<ExampleResponse> result = this.exampleService.searchExampleByKeyword(pathValue);


        return new CommonResponse(true, HttpStatus.OK, "리스트 조회에 성공했습니다", result);
    }


    @PostMapping("/error")
    @Operation(summary = "EXAM_API_03 : 예외 테스트", description = "예외를 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "일부로 403 오류 발생 케이스",
                    content = {@Content(schema = @Schema(implementation = SwaggerErrorResponseType.class))}),
            @ApiResponse(responseCode = "404", description = "일부로 404 오류 발생 케이스",
                    content = {@Content(schema = @Schema(implementation = SwaggerErrorResponseType.class))}),
            @ApiResponse(responseCode = "415", description = "일부로 415 오류 발생 케이스",
                    content = {@Content(schema = @Schema(implementation = SwaggerErrorResponseType.class))})
    })
    public CommonResponse<List<ExampleResponse>> throwExceptionApi(
            @Valid @RequestBody ExampleValidationRequest request
    ) {
        if(request.getErrorCode() == 500){
            // 예상치 못한 오류 발생시키기
            int[] array = {1,2,3,4,5};
            System.out.println(array[50]);
        }

        if(request.getErrorCode() == 403){
            throw new ForbiddenException403(ErrorDefineCode.EXAMPLE_OCCURE_ERROR);
        }

        if(request.getErrorCode() == 404){
            throw new NoSuchElementFoundException404(ErrorDefineCode.EXAMPLE_OCCURE_ERROR);
        }

        if(request.getErrorCode() == 415){
            throw new UnsupportedMediaTypeException415(ErrorDefineCode.EXAMPLE_OCCURE_ERROR);
        }

        return new CommonResponse(true, HttpStatus.OK, "성공입니당", null);
    }




    @GetMapping("/auth/all")
    @PreAuthorize(AuthConstant.AUTH_ALL)
    @Operation(summary = "EXAM_AUTH_01 : 인증 테스트 (전체)", description = "인증된 회원 사용 가능(교수/학생 모두) \\\n" +
            "curl -X 'GET' \\\n" +
            "  'http://localhost:8080/api/v1/example/auth/all' \\\n" +
            "  -H 'accept: application/json' \\\n" +
            "  -H 'Authorization: Bearer YOUR_ACCESS_TOKEN'")
    @ApiResponses(value = {
    })
    public CommonResponse<String> exampleAuthPassByAll(

    ) {
        String result = SecurityUtil.getCurrentMemberEmail();   //현재 요청 주체의 이메일 추출
        return new CommonResponse(true, HttpStatus.OK, "인가 성공으로 API 사용완료", result);
    }

    @GetMapping("/auth/user")
    @PreAuthorize(AuthConstant.AUTH_USER)
    @Operation(summary = "EXAM_AUTH_02 : 인증 테스트 (학생)", description = "인증된 학생(USER) 권한만 사용 가능")
    @ApiResponses(value = {
    })
    public CommonResponse<String> exampleAuthPassByUserRole(

    ) {
        String result = SecurityUtil.getCurrentMemberEmail();   //현재 요청 주체의 이메일 추출
        return new CommonResponse(true, HttpStatus.OK, "인가 성공으로 API 사용완료", result);
    }

    @GetMapping("/auth/admin")
    @PreAuthorize(AuthConstant.AUTH_ADMIN)
    @Operation(summary = "EXAM_AUTH_03 : 인증 테스트 (교수)", description = "인증된 교수(ADMIN) 권한만 사용 가능")
    @ApiResponses(value = {
    })
    public CommonResponse<String> exampleAuthPassByAdminRole(

    ) {
        String result = SecurityUtil.getCurrentMemberEmail();   //현재 요청 주체의 이메일 추출
        return new CommonResponse(true, HttpStatus.OK, "인가 성공으로 API 사용완료", result);
    }


}
