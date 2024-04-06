package com.didacto.service.enrollement;

import com.didacto.service.enrollment.EnrollmentService;
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


@SpringBootTest
@Transactional
@DisplayName("Enrollment Service")
public class EnrollmentServiceTest {

    @Autowired
    private EnrollmentService exampleService;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void before(){

    }

    @AfterEach
    public void after(){

    }

    @Test
    @DisplayName("Enrollment : (학생) 강의 등록 요청")
    public void testEnrollment_Request_Enrollment() throws Exception {

    }

    @Test
    @DisplayName("Enrollment : (학생) 강의 등록 요청 취소")
    public void testEnrollment_Cancel_Enrollment() throws Exception {

    }

    @Test
    @DisplayName("Enrollment : (교수) 강의 등록 요청 승인")
    public void testEnrollment_Accept_Enrollment() throws Exception {

    }

    @Test
    @DisplayName("Enrollment : (교수) 강의 등록 요청 거절")
    public void testEnrollment_Reject_Enrollment() throws Exception {

    }



}
