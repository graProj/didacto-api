package com.didacto.service.enrollment;


import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.*;
import com.didacto.dto.enrollment.EnrollmentQueryFilter;
import com.didacto.dto.enrollment.EnrollmentResponse;
import com.didacto.repository.enrollment.EnrollmentRepository;
import com.didacto.repository.lecture.LectureRepository;
import com.didacto.repository.member.MemberRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Disabled
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class EnrollmentQueryServiceTest {

    @Autowired
    private EnrollmentQueryService enrollmentQueryService;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private LectureRepository lectureRepository;

    @DisplayName("ID를 통해 초대 정보를 조회할 수 있다.")
    @Test
    void getEnrollmentById(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        memberRepository.saveAll(List.of(student, tutor));

        Lecture lecture1 = createLecture("L1", tutor);
        lectureRepository.save(lecture1);

        Enrollment enrollmentTarget = createEnrollment(lecture1, student, EnrollmentStatus.ACCEPTED, tutor);
        Enrollment enrollment1 = createEnrollment(lecture1, student, EnrollmentStatus.REJECTED, tutor);
        Enrollment enrollment2 = createEnrollment(lecture1, student, EnrollmentStatus.CANCELLED, tutor);
        enrollmentTarget = enrollmentRepository.save(enrollmentTarget);
        enrollmentRepository.saveAll(List.of(enrollment1, enrollment2));

        // when
        EnrollmentResponse resultEnrollment = enrollmentQueryService.getEnrollmentById(enrollmentTarget.getId());

        // then
        assertThat(resultEnrollment).isNotNull();
        assertThat(resultEnrollment.getLecture().getTitle()).isEqualTo("L1");
        assertThat(resultEnrollment.getMember().getName()).isEqualTo("S1");
        assertThat(resultEnrollment.getId()).isEqualTo(enrollmentTarget.getId());
        assertThat(resultEnrollment.getStatus()).isEqualTo(EnrollmentStatus.ACCEPTED);

    }

    @DisplayName("ID를 통해 초대 정보를 조회할 때 존재하지 않으면 예외가 발생한다.")
    @Test
    void getEnrollmentByNonExistId(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        memberRepository.saveAll(List.of(student, tutor));

        Lecture lecture1 = createLecture("L1", tutor);
        lectureRepository.save(lecture1);

        Enrollment enrollmentTarget = createEnrollment(lecture1, student, EnrollmentStatus.ACCEPTED, tutor);
        enrollmentTarget = enrollmentRepository.save(enrollmentTarget);

        // when, then
        Long searchId = enrollmentTarget.getId() + 10;

        assertThatThrownBy(() -> enrollmentQueryService.getEnrollmentById(searchId))
                .isInstanceOf(NoSuchElementFoundException404.class)
                .hasMessage("해당 초대 정보를 찾을 수 없습니다.");

    }








    private Member createMember(
            String email, String name, Grade grade, Authority role, Boolean deleted){
        return Member.builder()
                .email(email)
                .name(name)
                .birth(getDummyDateTime())
                .password("1234")
                .grade(grade)
                .role(role)
                .deleted(deleted)
                .build();
    }

    private Lecture createLecture(
            String title, Member owner){
        return Lecture.builder()
                .title(title)
                .owner(owner)
                .state(LectureState.WAITING)
                .startTime(getDummyDateTime())
                .endTime(getDummyDateTime())
                .build();
    }

    private Enrollment createEnrollment(
            Lecture lecture, Member member, EnrollmentStatus status, Member modified_member){
        return Enrollment.builder()
                .lecture(lecture)
                .member(member)
                .status(status)
                .modified_by(modified_member)
                .build();
    }
    private EnrollmentQueryFilter createQueryFilter(
            Long lectureId, Long memberId, Long tutorId, List<EnrollmentStatus> status, List<Long> ids){

        return EnrollmentQueryFilter.builder()
                .lectureId(lectureId)
                .memberId(memberId)
                .tutorId(tutorId)
                .statuses(status)
                .ids(ids).build();
    }

    private OffsetDateTime getDummyDateTime(){
        return OffsetDateTime.of(2024, 8, 5, 10, 20, 0, 0, ZoneOffset.UTC);
    }

}