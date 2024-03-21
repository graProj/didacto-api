package com.didacto.service.example;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.AlreadyExistElementException409;
import com.didacto.domain.Example;
import com.didacto.dto.example.ExampleRequestDto;
import com.didacto.dto.example.ExampleResponseDto;
import com.didacto.repository.example.ExampleRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ExampleServiceImpl implements ExampleService {

    private final ExampleRepository exampleRepository;
    private final ModelMapper modelMapper;


    @Transactional
    public Long addExample(ExampleRequestDto input){
        Example exist = exampleRepository.findFirstByName(input.getName());
        if(exist != null){
            throw new AlreadyExistElementException409(ErrorDefineCode.DUPLICATE_EXAMPLE_NAME);
        }

        Example example = modelMapper.map(input, Example.class);
        example = exampleRepository.save(example);
        return example.getExamId();
    }

    public List<ExampleResponseDto> searchExampleByKeyword(String keyword) {
        List<Example> example = exampleRepository.findExamplesWithKeywordContains(keyword);
        List<ExampleResponseDto> response = example.stream()
                .map(e -> modelMapper.map(e, ExampleResponseDto.class))
                .toList();
        return response;
    }
}
