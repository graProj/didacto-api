package com.didacto.service.enrollement;

import com.didacto.domain.*;
import com.didacto.dto.enrollment.EnrollmentQueryFilter;
import com.didacto.repository.enrollment.EnrollmentRepository;
import com.didacto.service.enrollment.EnrollmentCommandService;
import com.didacto.service.enrollment.EnrollmentQueryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@Transactional
@DisplayName("Enrollment Query Service")
@ExtendWith(MockitoExtension.class)
public class EnrollmentQueryServiceTest {
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @InjectMocks
    private EnrollmentQueryService enrollmentQueryService;

    @BeforeEach
    public void before(){

    }

    @AfterEach
    public void after(){

    }

    @Test
    @DisplayName("해당 Student가 Lecutre에 이미 초대 요청을 보내 대기중인 Enrollment가 있는지 확인 ")
    public void existWaitingEnrollmentTest() throws Exception {

        Enrollment enrollment = givenEnrollment();


        given(enrollmentRepository.findEnrollment(
                EnrollmentQueryFilter.builder()
                        .memberId(1L)
                        .lectureId(1L)
                        .statuses(List.of(EnrollmentStatus.WAITING))
                        .build()
        )).willReturn(Optional.ofNullable(enrollment));


        //when
        boolean exist = enrollmentQueryService.existWaitingEnrollmentByMemberId(1L, 1L);

        //then
        assertThat(exist).isEqualTo(true);

    }

    private Enrollment givenEnrollment(){
        //given
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

        Lecture lecture = Lecture.builder()
                .title("ENROLLTEST_LECTURE")
                .owner(tutor)
                .state(LectureState.WAITING)
                .build();

        Enrollment enrollment = Enrollment.builder()
                .member(student)
                .lecture(lecture)
                .status(EnrollmentStatus.WAITING)
                .modified_by(student)
                .id(1L)
                .build();

        return enrollment;
    }
}
