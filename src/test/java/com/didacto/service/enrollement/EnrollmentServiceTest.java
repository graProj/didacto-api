package com.didacto.service.enrollement;

import com.didacto.domain.*;
import com.didacto.dto.enrollment.EnrollmentBasicResponse;
import com.didacto.dto.enrollment.EnrollmentListResponse;
import com.didacto.dto.enrollment.EnrollmentQueryConditionRequest;
import com.didacto.dto.enrollment.EnrollmentRequest;
import com.didacto.service.enrollment.EnrollmentCommandService;
import com.didacto.service.enrollment.EnrollmentQueryService;
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
    private EnrollmentCommandService enrollmentCommandService;

    @Autowired
    private EnrollmentQueryService enrollmentQueryService;

    @Autowired
    private EntityManager em;

    private Long studentId;
    private Long studentId2;
    private Long tutorId;
    private Long lectureId;
    private Long lectureId2;

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
        Long enrollment = enrollmentCommandService.requestEnrollment(lectureId, studentId).getId();

        //then
        assertThat(enrollment).isNotNull();

    }

    @Test
    @DisplayName("Enrollment : (학생) 강의 등록 요청 취소")
    public void testEnrollment_Cancel_Enrollment() throws Exception {
        //given
        EnrollmentRequest request = new EnrollmentRequest(lectureId);
        Long id = enrollmentCommandService.requestEnrollment(request.getLectureId(), studentId).getId();

        //when
        id = enrollmentCommandService.cancelEnrollment(id, studentId).getId();

        //then
        EnrollmentBasicResponse enrollment =  enrollmentQueryService.getEnrollmentById(id);
        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.CANCELLED);

    }

    @Test
    @DisplayName("Enrollment : (교수) 강의 등록 요청 승인")
    public void testEnrollment_Accept_Enrollment() throws Exception {
        //given
        EnrollmentRequest request = new EnrollmentRequest(lectureId);
        Long id = enrollmentCommandService.requestEnrollment(request.getLectureId(), studentId).getId();

        //when
        Long enrollId = enrollmentCommandService.confirmEnrollment(id, tutorId, EnrollmentStatus.ACCEPTED).getId();


        //then
        EnrollmentBasicResponse enrollment =  enrollmentQueryService.getEnrollmentById(id);
        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.ACCEPTED);
    }

    @Test
    @DisplayName("Enrollment : (교수) 강의 등록 요청 거절")
    public void testEnrollment_Reject_Enrollment() throws Exception {
        //given
        EnrollmentRequest request = new EnrollmentRequest(lectureId);
        Long id = enrollmentCommandService.requestEnrollment(request.getLectureId(), studentId).getId();

        //when
        id = enrollmentCommandService.confirmEnrollment(id, tutorId, EnrollmentStatus.REJECTED).getId();

        //then
        EnrollmentBasicResponse enrollment =  enrollmentQueryService.getEnrollmentById(id);
        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.REJECTED);
    }

    @Test
    @DisplayName("Enrollment : 강의 등록요청 목록 조회 : 강의실 ID로 조회")
    public void testEnrollment_Query_Enrollment_ByLectureId() throws Exception {

        // given
        enrollListGiven();

        // when
        EnrollmentQueryConditionRequest getAll = new EnrollmentQueryConditionRequest(1L, 10L, true, true, true, true);
        EnrollmentQueryConditionRequest getAccept = new EnrollmentQueryConditionRequest(1L, 10L, false, false, true, false);
        EnrollmentQueryConditionRequest getReject = new EnrollmentQueryConditionRequest(1L, 10L, false, false, false, true);
        EnrollmentListResponse enrollments =  enrollmentQueryService.getEnrollmentInfoList(lectureId, null, getAll, "date");
        EnrollmentListResponse enrollments2 =  enrollmentQueryService.getEnrollmentInfoList(lectureId, null, getAccept, "date");
        EnrollmentListResponse enrollments3 =  enrollmentQueryService.getEnrollmentInfoList(lectureId, null, getReject, "date");

        // then
        // 총 등록요청 개수 4개
        assertThat(enrollments.getEnrollments().size()).isEqualTo(4);
        // 총 Accept 개수 2개
        assertThat(enrollments2.getEnrollments().size()).isEqualTo(2);
        // 총 Reject 개수 1개
        assertThat(enrollments3.getEnrollments().size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Enrollment : 강의 목록 조회 : 학생 ID로 조회")
    public void testEnrollment_Query_Enrollment_ByStudentId() throws Exception {
        // given
        enrollListGiven();

        // when
        EnrollmentQueryConditionRequest getAll = new EnrollmentQueryConditionRequest(1L, 10L, true, true, true, true);
        EnrollmentQueryConditionRequest getAccept = new EnrollmentQueryConditionRequest(1L, 10L, false, false, true, false);
        EnrollmentQueryConditionRequest getReject = new EnrollmentQueryConditionRequest(1L, 10L, false, false, false, true);
        EnrollmentListResponse enrollments =  enrollmentQueryService.getEnrollmentInfoList(null, studentId, getAll, "date");
        EnrollmentListResponse enrollments2 =  enrollmentQueryService.getEnrollmentInfoList(null, studentId, getAccept, "date");
        EnrollmentListResponse enrollments3 =  enrollmentQueryService.getEnrollmentInfoList(null, studentId, getReject, "date");

        // then
        // 총 등록요청 개수 4개
        assertThat(enrollments.getEnrollments().size()).isEqualTo(4);
        // 총 Accept 개수 1개
        assertThat(enrollments2.getEnrollments().size()).isEqualTo(1);
        // 총 Reject 개수 2개
        assertThat(enrollments3.getEnrollments().size()).isEqualTo(2);

    }

    @Test
    @DisplayName("Enrollment : 강의 목록 조회 : 페이지네이션")
    public void testEnrollment_Query_Enrollment_Pagenation() throws Exception {
        //given
        enrollListGiven();

        //when
        EnrollmentQueryConditionRequest request = new EnrollmentQueryConditionRequest(1L, 3L, true, true, true, true);
        EnrollmentQueryConditionRequest request2 = new EnrollmentQueryConditionRequest(2L, 3L, true, true, true, true);
        EnrollmentListResponse enrollments =  enrollmentQueryService.getEnrollmentInfoList(lectureId, null, request, "date");
        EnrollmentListResponse enrollments2 =  enrollmentQueryService.getEnrollmentInfoList(lectureId, null, request2, "date");

        //then
        assertThat(enrollments.getEnrollments().size()).isEqualTo(3);
        assertThat(enrollments.getPageInfo().getHaveNext()).isEqualTo(true);
        assertThat(enrollments2.getEnrollments().size()).isEqualTo(1);
        assertThat(enrollments2.getPageInfo().getHaveNext()).isEqualTo(false);

    }

    @Test
    @DisplayName("Enrollment : 강의 목록 조회 : 필터링")
    public void testEnrollment_Query_Enrollment_Filter() throws Exception {
        //given
        enrollListGiven();

        //when
        EnrollmentQueryConditionRequest request = new EnrollmentQueryConditionRequest(1L, 10L, false, false, true, true);
        EnrollmentListResponse enrollments =  enrollmentQueryService.getEnrollmentInfoList(lectureId, null, request, "date");

        //then
        assertThat(enrollments.getEnrollments().size()).isEqualTo(3);

    }

    private void enrollListGiven(){
        // given : Student 1 -> Lecture 1 등록요청
        // Cancel 1, Reject 1, Accept 1
        EnrollmentRequest givenRequest = new EnrollmentRequest(lectureId);
        Long id = enrollmentCommandService.requestEnrollment(givenRequest.getLectureId(), studentId).getId();
        id = enrollmentCommandService.cancelEnrollment(id, studentId).getId();
        id = enrollmentCommandService.requestEnrollment(givenRequest.getLectureId(), studentId).getId();
        id = enrollmentCommandService.confirmEnrollment(id, tutorId, EnrollmentStatus.REJECTED).getId();
        id = enrollmentCommandService.requestEnrollment(givenRequest.getLectureId(), studentId).getId();
        id = enrollmentCommandService.confirmEnrollment(id, tutorId, EnrollmentStatus.ACCEPTED).getId();

        // given : Student 2 -> Lecture 1 등록요청
        // Accept 1
        id = enrollmentCommandService.requestEnrollment(givenRequest.getLectureId(), studentId2).getId();
        id = enrollmentCommandService.confirmEnrollment(id, tutorId, EnrollmentStatus.ACCEPTED).getId();

        // given : Student 1 -> Lecture 2 등록요청
        // Accept 1
        givenRequest = new EnrollmentRequest(lectureId2);
        id = enrollmentCommandService.requestEnrollment(givenRequest.getLectureId(), studentId).getId();
        id = enrollmentCommandService.confirmEnrollment(id, tutorId, EnrollmentStatus.REJECTED).getId();
    }




    private void initializeData(){
        Member student = Member.builder()
                .email("enrollstd@test.com")
                .password("test123!")
                .name("학생1")
                .role(Authority.ROLE_USER)
                .birth(OffsetDateTime.now())
                .build();

        Member tutor = Member.builder()
                .email("enrolltec@test.com")
                .password("test123!")
                .name("교수1")
                .role(Authority.ROLE_ADMIN)
                .birth(OffsetDateTime.now())
                .build();

        Member student2 = Member.builder()
                .email("enrollstd2@test.com")
                .password("test123!")
                .name("학생2")
                .role(Authority.ROLE_USER)
                .birth(OffsetDateTime.now())
                .build();

        em.persist(student);
        em.persist(tutor);
        em.persist(student2);
        em.clear();
        em.flush();

        this.studentId = student.getId();
        this.tutorId = tutor.getId();
        this.studentId2 = student2.getId();


        Lecture lecture = Lecture.builder()
                .title("ENROLLTEST_LECTURE")
                .owner(tutor)
                .state(LectureState.WAITING)
                .build();

        Lecture lecture2 = Lecture.builder()
                .title("ENROLLTEST_LECTURE2")
                .owner(tutor)
                .state(LectureState.WAITING)
                .build();

        em.persist(lecture);
        em.persist(lecture2);
        em.clear();
        em.flush();

        this.lectureId = lecture.getId();
        this.lectureId2 = lecture2.getId();
    }


}
