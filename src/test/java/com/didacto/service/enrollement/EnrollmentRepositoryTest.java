package com.didacto.service.enrollement;
import com.didacto.common.OffsetDateTimeProvider;
import com.didacto.config.querydsl.QueryDslConfig;
import com.didacto.domain.*;
import com.didacto.dto.enrollment.EnrollmentQueryFilter;
import com.didacto.repository.enrollment.EnrollmentRepository;
import com.didacto.repository.lecture.LectureRepository;
import com.didacto.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Enrollment Repository Test")
@Import({QueryDslConfig.class, OffsetDateTimeProvider.class})
public class EnrollmentRepositoryTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LectureRepository lectureRepository;

    Member student;
    Member tutor;
    Lecture lecture;
    Enrollment waitEnrollment;

    @BeforeEach
    public void before(){
        this.initializeData();
    }

    @Test
    @DisplayName("해당 Member가 Lecutre에 이미 요청한 Enrollment(대기중) 가 존재하는지 확인")
    public void findExistWaitingEnrollmentTest(){
        //given
        givenEnrollment();

        //when
        Optional<Enrollment> enrollment = enrollmentRepository.findEnrollment(
                EnrollmentQueryFilter.builder()
                        .memberId(student.getId())
                        .lectureId(lecture.getId())
                        .statuses(List.of(EnrollmentStatus.WAITING))
                        .build()
        );

        //then
        assertThat(enrollment.get().getMember().getId()).isEqualTo(student.getId());
        assertThat(enrollment.get().getLecture().getId()).isEqualTo(lecture.getId());
        assertThat(enrollment.get().getLecture().getOwner().getId()).isEqualTo(tutor.getId());
    }


    private void givenEnrollment(){
        waitEnrollment = Enrollment.builder()
                .status(EnrollmentStatus.WAITING)
                .lecture(lecture)
                .member(student)
                .modified_by(student)
                .build();

        waitEnrollment = enrollmentRepository.save(waitEnrollment);

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


        student = memberRepository.save(student);
        tutor = memberRepository.save(tutor);


        lecture = Lecture.builder()
                .title("ENROLLTEST_LECTURE")
                .owner(tutor)
                .state(LectureState.WAITING)
                .build();

        lecture = lectureRepository.save(lecture);
    }
}
