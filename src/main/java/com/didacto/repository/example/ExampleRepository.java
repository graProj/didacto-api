package com.didacto.repository.example;

import com.didacto.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long>, ExampleCustomRepository {

    // JPQL의 사용은 지양한다.

    // Spring Data JPA 사용
    // 메서드 명명을 통한 기본적인 CRUD 작업은 Spring Data JPA를 사용하여 구현
    Example findExampleByName(String name);


    // QueryDSL 사용
    // Spring Data JPA로는 달성할 수 없는 복잡한 쿼리나 동적 쿼리에는 CustomRepository에서 QueryDSL을 사용
    List<Example> findExamplesWithKeywordContains(String email);


}


