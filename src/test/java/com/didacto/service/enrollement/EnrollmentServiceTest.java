package com.didacto.service.enrollement;

import com.didacto.domain.*;
import com.didacto.dto.enrollment.EnrollmentBasicTypeResponse;
import com.didacto.dto.enrollment.EnrollmentCancelRequest;
import com.didacto.dto.enrollment.EnrollmentConfirmRequest;
import com.didacto.dto.enrollment.EnrollmentRequest;
import com.didacto.dto.example.ExampleRequest;
import com.didacto.service.enrollment.EnrollmentService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@DisplayName("Enrollment Service")
public class EnrollmentServiceTest {

    @Autowired
    private EnrollmentService exampleService;

    @Autowired
    private EntityManager em;

    private Long studentId;
    private Long tutorId;
    private Long lectureId;

    @BeforeEach
    public void before(){
        this.initializeData();
    }

    @AfterEach
    public void after(){

    }

    @Test
    @DisplayName("Enrollment : (학생) 강의 등록 요청")
    public void testEnrollment_Request_Enrollment() throws Exception {
        //given

        //when
        Long enrollment = exampleService.requestEnrollment(lectureId, studentId);

        //then
        assertThat(enrollment).isNotNull();

    }

    @Test
    @DisplayName("Enrollment : (학생) 강의 등록 요청 취소")
    public void testEnrollment_Cancel_Enrollment() throws Exception {
        //given
        EnrollmentRequest request = new EnrollmentRequest(lectureId);
        Long enrollment = exampleService.requestEnrollment(request.getLectureId(), studentId);

//        //when
//        enrollment = exampleService.cancelEnrollment(enrollment.getId(), studentId);
//
//        //then
//        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.CANCELLED);

    }

    @Test
    @DisplayName("Enrollment : (교수) 강의 등록 요청 승인")
    public void testEnrollment_Accept_Enrollment() throws Exception {
        //given
        EnrollmentRequest request = new EnrollmentRequest(lectureId);
        Long enrollment = exampleService.requestEnrollment(request.getLectureId(), studentId);

//        //when
//        enrollment = exampleService.confirmEnrollment(enrollment.getId(), tutorId, EnrollmentStatus.ACCEPTED);
//
//        //then
//        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.ACCEPTED);
    }

    @Test
    @DisplayName("Enrollment : (교수) 강의 등록 요청 거절")
    public void testEnrollment_Reject_Enrollment() throws Exception {
        //given
        EnrollmentRequest request = new EnrollmentRequest(lectureId);
        Long enrollment = exampleService.requestEnrollment(request.getLectureId(), studentId);

//        //when
//        enrollment = exampleService.confirmEnrollment(enrollment.getId(), tutorId, EnrollmentStatus.REJECTED);
//
//        //then
//        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.REJECTED);
    }


    private void initializeData(){
        Member student = Member.builder()
                .email("enrollstd@test.com")
                .password("test123!")
                .name("학생")
                .role(Authority.ROLE_USER)
                .birth(OffsetDateTime.now())
                .build();

        Member tutor = Member.builder()
                .email("enrolltec@test.com")
                .password("test123!")
                .name("교수")
                .role(Authority.ROLE_ADMIN)
                .birth(OffsetDateTime.now())
                .build();

        em.persist(student);
        em.persist(tutor);
        em.clear();
        em.flush();

        this.studentId = student.getId();
        this.tutorId = tutor.getId();


        Lecture lecture = Lecture.builder()
                .title("ENROLLTEST_LECTURE")
                .owner(tutor)
                .state(LectureState.WAITING)
                .deleted(false)
                .build();

        em.persist(lecture);
        em.clear();
        em.flush();

        this.lectureId = lecture.getId();
    }


}
