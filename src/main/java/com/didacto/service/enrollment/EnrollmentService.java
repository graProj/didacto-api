package com.didacto.service.enrollment;


import com.didacto.dto.example.ExampleRequest;
import com.didacto.repository.enrollment.EnrollmentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final ModelMapper modelMapper;


    /**
     * [Title]
     * Description
     * @param input Dto - Desc
     * @return Long - DEsc
     */
    @Transactional
    public Long add(ExampleRequest input){
        return 1L;
    }




}
