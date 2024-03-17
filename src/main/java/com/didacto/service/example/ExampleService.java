package com.didacto.service.example;

import com.didacto.dto.example.ExampleRequestDto;
import com.didacto.dto.example.ExampleResponseDto;

import java.util.List;

public interface ExampleService {

    /**
     * [예제 도메인 추가]
     * 예제 도메인을 데이터베이스에 신규 추가한다.
     * @param member ExampleRequestDto - 추가할 유저 정보를 담은 DTO 객체
     * @return Long - 추가된 유저의 ID
     */
    Long addExample(ExampleRequestDto member);

    /**
     * [예제 도메인 탐색]
     * Keyword를 통해 예제 도메인을 탐색하여 첫 번째 탐색 결과를 반환한다.
     * @param keyword String - 예제 도메인의 검색 키워드
     * @return ExampleResponseDto - 탐색 결과 단일 도메인
     */
    List<ExampleResponseDto> searchExampleByKeyword(String keyword);


}
