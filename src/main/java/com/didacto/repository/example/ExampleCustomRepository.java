package com.didacto.repository.example;

import com.didacto.domain.Example;

import java.util.List;

public interface ExampleCustomRepository {

    List<Example> findExamplesWithKeywordContains(String keyword);
}
