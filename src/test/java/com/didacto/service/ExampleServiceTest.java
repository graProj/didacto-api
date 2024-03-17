package com.didacto.service;


import com.didacto.domain.Example;
import com.didacto.dto.example.ExampleRequestDto;
import com.didacto.dto.example.ExampleResponseDto;
import com.didacto.service.example.ExampleService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 서비스 코드에 대해 given / when / then의 형식의 단위 테스트 코드 작성을 수행한다.
 순서는 테스트 시나리오 작성 -> 실제 개발 진행 -> 서비스 코드의 검증 코드 작성에 따른다.

 메서드명은 주로 다음과 같은 형식을 따른다.
 테스트 대상 메서드의 동작을 설명하는 이름: 메서드의 기능이나 목적을 이해하기 쉽게 메서드명을 작성
 테스트 시나리오를 반영하는 이름: 어떤 시나리오를 테스트하는지 명확하게 드러내는 이름을 선택
 메서드의 상태 또는 입력을 나타내는 이름: 테스트할 메서드의 입력이나 상태에 대한 정보를 포함할 수 있음
 예상되는 결과 또는 행동을 포함하는 이름: 테스트가 완료되었을 때 예상되는 결과나 메서드의 예상 행동을 명시

 */
@SpringBootTest
@Transactional // 각각의 테스트 메서드에 대해 트랜잭션을 시작하고, 테스트가 종료되면 롤백
@DisplayName("Example Service")
public class ExampleServiceTest {

    @Autowired
    private ExampleService exampleService;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void before(){
        // When Before Test, set someting
        ExampleRequestDto example = new ExampleRequestDto("Someting example content");
        Long result = exampleService.addExample(example);
    }

    @AfterEach
    public void after(){
        // When After Test, work someting
    }

    @Test
    @DisplayName("Example : 도메인 추가 로직 성공 검증")
    public void testProcessExample_ValidInsert_Success() throws Exception {
        // Given
        ExampleRequestDto request = new ExampleRequestDto("Some");

        // When
        Long id = exampleService.addExample(request);

        // Then
        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("Example : 키워드로 검색 성공 검증")
    public void testProcessExample_ValidSearchByKeyword_Success() throws Exception {
        // Given
        String given = "Someting";

        // When
        List<ExampleResponseDto> list = exampleService.searchExampleByKeyword(given);

        // Then
        assertThat(list).anyMatch(element -> element.getName().equals("Someting example content"));
    }


}
