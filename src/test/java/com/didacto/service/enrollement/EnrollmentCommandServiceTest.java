package com.didacto.service.enrollement;

import com.didacto.domain.*;
import com.didacto.dto.enrollment.EnrollmentQueryFilter;
import com.didacto.dto.lecturemember.LectureMemberQueryFilter;
import com.didacto.repository.enrollment.EnrollmentRepository;
import com.didacto.repository.lecturemember.LectureMemberRepository;
import com.didacto.service.enrollment.EnrollmentCommandService;
import com.didacto.service.enrollment.EnrollmentQueryService;
import com.didacto.service.lecture.LectureQueryService;
import com.didacto.service.lecturemember.LectureMemberCommandService;
import com.didacto.service.lecturemember.LectureMemberQueryService;
import com.didacto.service.member.MemberQueryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@Transactional
@DisplayName("Enrollment Command Service")
@ExtendWith(MockitoExtension.class)
public class EnrollmentCommandServiceTest {
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private LectureQueryService lectureQueryService;
    @Mock
    private MemberQueryService memberQueryService;
    @Mock
    private LectureMemberQueryService lectureMemberQueryService;
    @Mock
    private EnrollmentQueryService enrollmentQueryService;

    @InjectMocks
    private EnrollmentCommandService enrollmentCommandService;


    private Member student;
    private Member tutor;
    private Lecture lecture;


    @BeforeEach
    public void before(){
        this.initializeData();
    }

    @AfterEach
    public void after(){

    }

    @Test
    @DisplayName("Enrollment : (학생) 강의 등록 요청")
    public void enrollmentRequest() throws Exception {

        //given
        given(lectureQueryService.queryOne(lecture.getId())).willReturn(lecture);
        given(memberQueryService.query(student.getId())).willReturn(student);
        given(enrollmentQueryService.existWaitingEnrollmentByMemberId(student.getId(), lecture.getId())).willReturn(false);
        given(lectureMemberQueryService.existLectureMember(student.getId(), lecture.getId())).willReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class))).then(AdditionalAnswers.returnsFirstArg());

        //when
        Enrollment enrollment = enrollmentCommandService.requestEnrollment(lecture.getId(), student.getId());

        //then
        assertThat(enrollment).isNotNull();
        assertThat(enrollment.getMember().getEmail()).isEqualTo(student.getEmail());
        assertThat(enrollment.getLecture().getTitle()).isEqualTo(lecture.getTitle());
        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.WAITING);


    }



    private void initializeData(){
        student = Member.builder()
                .email("enrollstd@test.com")
                .password("test123!")
                .name("학생1")
                .role(Authority.ROLE_USER)
                .birth(OffsetDateTime.now())
                .build();

        tutor = Member.builder()
                .email("enrolltec@test.com")
                .password("test123!")
                .name("교수1")
                .role(Authority.ROLE_ADMIN)
                .birth(OffsetDateTime.now())
                .build();

        lecture = Lecture.builder()
                .title("ENROLLTEST_LECTURE")
                .owner(tutor)
                .state(LectureState.WAITING)
                .build();

    }


}
