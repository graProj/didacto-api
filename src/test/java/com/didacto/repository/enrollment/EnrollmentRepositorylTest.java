package com.didacto.repository.enrollment;

import com.didacto.domain.*;
import com.didacto.dto.enrollment.EnrollmentQueryFilter;
import com.didacto.repository.lecture.LectureRepository;
import com.didacto.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class EnrollmentRepositorylTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private LectureRepository lectureRepository;

    @DisplayName("초대 단건 검색 시 초대 목록중 특정 학생으로 검색했을 때 정확히 한 개를 조회할 수 있다.")
    @Test
    void findEnrollmentByStudent(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member student2 = createMember("S2@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        student = memberRepository.save(student);
        memberRepository.saveAll(List.of(student2, tutor));

        Lecture lecture = createLecture("L1", tutor);
        lectureRepository.saveAll(List.of(lecture));

        Enrollment enrollmentTarget = createEnrollment(lecture, student, EnrollmentStatus.CANCELLED, tutor);
        Enrollment enrollment = createEnrollment(lecture, student2, EnrollmentStatus.WAITING, tutor);
        enrollmentRepository.saveAll(List.of(enrollmentTarget, enrollment));

        EnrollmentQueryFilter filter = createQueryFilter(null, student.getId(), null, null, null);

        // when
        Optional<Enrollment> foundedEnrollment = enrollmentRepository.findEnrollment(filter);

        // then
        assertThat(foundedEnrollment).isNotNull();
        assertThat(foundedEnrollment.get().getMember().getId()).isEqualTo(student.getId());
        assertThat(foundedEnrollment.get().getMember().getName()).isEqualTo("S1");
        assertThat(foundedEnrollment.get().getLecture().getTitle()).isEqualTo("L1");
        assertThat(foundedEnrollment.get().getStatus()).isEqualTo(EnrollmentStatus.CANCELLED);
    }

    @DisplayName("초대 단건 검색 시 검색 조건과 일치하는 초대가 두 개 이상 존재하면 검색에 실패한다.")
    @Test
    void findEnrollmentByStudentDuplicateFail(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        student = memberRepository.save(student);
        tutor = memberRepository.save(tutor);

        Lecture lecture = createLecture("L1", tutor);
        lectureRepository.saveAll(List.of(lecture));

        Enrollment enrollment1 = createEnrollment(lecture, student, EnrollmentStatus.CANCELLED, tutor);
        Enrollment enrollment2 = createEnrollment(lecture, student, EnrollmentStatus.WAITING, tutor);
        enrollmentRepository.saveAll(List.of(enrollment1, enrollment2));

        EnrollmentQueryFilter filter = createQueryFilter(null, student.getId(), null, null, null);

        // when, then

        assertThatThrownBy(() -> enrollmentRepository.findEnrollment(filter))
                .isInstanceOf(com.querydsl.core.NonUniqueResultException.class);
    }

    @DisplayName("초대 단건 검색 시 특정 강의에 해당하는 조건으로 정확히 한 개 조회할 수 있다.")
    @Test
    void findEnrollmentByLecture(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        memberRepository.saveAll(List.of(student, tutor));

        Lecture lecture1 = createLecture("L1", tutor);
        Lecture lecture2 = createLecture("L2", tutor);
        lecture1 = lectureRepository.save(lecture1);
        lecture2 = lectureRepository.save(lecture2);

        Enrollment enrollmentTarget = createEnrollment(lecture1, student, EnrollmentStatus.CANCELLED, tutor);
        Enrollment enrollment = createEnrollment(lecture2, student, EnrollmentStatus.WAITING, tutor);
        enrollmentRepository.saveAll(List.of(enrollmentTarget, enrollment));

        EnrollmentQueryFilter filter = createQueryFilter(lecture1.getId(), null, null, null, null);

        // when
        Optional<Enrollment> foundedEnrollment = enrollmentRepository.findEnrollment(filter);

        // then
        assertThat(foundedEnrollment).isNotNull();
        assertThat(foundedEnrollment.get().getMember().getName()).isEqualTo("S1");
        assertThat(foundedEnrollment.get().getLecture().getId()).isEqualTo(lecture1.getId());
        assertThat(foundedEnrollment.get().getLecture().getTitle()).isEqualTo("L1");
        assertThat(foundedEnrollment.get().getStatus()).isEqualTo(EnrollmentStatus.CANCELLED);
    }

    @DisplayName("초대 단건 검색 시 특정 교수자에 해당하는 조건으로 초대를 정확히 한 개 조회할 수 있다.")
    @Test
    void findEnrollmentByTutor(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        Member tutor2 = createMember("T2@email.com", "T2", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        student = memberRepository.save(student);
        tutor = memberRepository.save(tutor);
        tutor2 = memberRepository.save(tutor2);

        Lecture lecture1 = createLecture("L1", tutor);
        Lecture lecture2 = createLecture("L2", tutor2);
        lectureRepository.saveAll(List.of(lecture1, lecture2));

        Enrollment enrollmentTarget = createEnrollment(lecture1, student, EnrollmentStatus.REJECTED, tutor);
        Enrollment enrollment = createEnrollment(lecture2, student, EnrollmentStatus.WAITING, tutor2);
        enrollmentRepository.saveAll(List.of(enrollmentTarget, enrollment));

        EnrollmentQueryFilter filter = createQueryFilter(null, null, tutor.getId(), null, null);

        // when
        Optional<Enrollment> foundedEnrollment = enrollmentRepository.findEnrollment(filter);

        // then
        assertThat(foundedEnrollment).isNotNull();
        assertThat(foundedEnrollment.get().getMember().getId()).isEqualTo(student.getId());
        assertThat(foundedEnrollment.get().getMember().getName()).isEqualTo("S1");
        assertThat(foundedEnrollment.get().getLecture().getTitle()).isEqualTo("L1");
        assertThat(foundedEnrollment.get().getStatus()).isEqualTo(EnrollmentStatus.REJECTED);
    }

    @DisplayName("초대 단건 검색 시 처리 상태에 해당하는 조건으로 초대를 정확히 한 개 조회할 수 있다.")
    @Test
    void findEnrollmentByStatus(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        memberRepository.saveAll(List.of(student, tutor));

        Lecture lecture1 = createLecture("L1", tutor);
        lecture1 = lectureRepository.save(lecture1);

        Enrollment enrollmentTarget = createEnrollment(lecture1, student, EnrollmentStatus.REJECTED, tutor);
        Enrollment enrollment = createEnrollment(lecture1, student, EnrollmentStatus.WAITING, tutor);
        enrollmentRepository.saveAll(List.of(enrollmentTarget, enrollment));

        EnrollmentQueryFilter filter = createQueryFilter(null, null, null, List.of(EnrollmentStatus.REJECTED), null);

        // when
        Optional<Enrollment> foundedEnrollment = enrollmentRepository.findEnrollment(filter);

        // then
        assertThat(foundedEnrollment).isNotNull();
        assertThat(foundedEnrollment.get().getMember().getName()).isEqualTo("S1");
        assertThat(foundedEnrollment.get().getStatus()).isEqualTo(EnrollmentStatus.REJECTED);
    }

    @DisplayName("초대 단건 검색 시 처리 상태에 대한 조건을 여러 개 설정하여 초대를 정확히 한 개 조회할 수 있다.")
    @Test
    void findEnrollmentByMultiStatus(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        memberRepository.saveAll(List.of(student, tutor));

        Lecture lecture1 = createLecture("L1", tutor);
        lecture1 = lectureRepository.save(lecture1);

        Enrollment enrollmentTarget = createEnrollment(lecture1, student, EnrollmentStatus.REJECTED, tutor);
        Enrollment enrollment = createEnrollment(lecture1, student, EnrollmentStatus.WAITING, tutor);
        enrollmentRepository.saveAll(List.of(enrollmentTarget, enrollment));

        EnrollmentQueryFilter filter = createQueryFilter(null, null, null,
                List.of(EnrollmentStatus.ACCEPTED, EnrollmentStatus.CANCELLED, EnrollmentStatus.REJECTED), null);

        // when
        Optional<Enrollment> foundedEnrollment = enrollmentRepository.findEnrollment(filter);

        // then
        assertThat(foundedEnrollment).isNotNull();
        assertThat(foundedEnrollment.get().getMember().getName()).isEqualTo("S1");
        assertThat(foundedEnrollment.get().getStatus()).isEqualTo(EnrollmentStatus.REJECTED);
    }


    @DisplayName("초대 단건 검색 시 [학생, 강의, 교수자, 초대 상태]의 복합 검색 조건으로 초대를 정확히 한 개 조회할 수 있다.")
    @Test
    void findEnrollmentWithComplexCondition(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member student2 = createMember("S2@email.com", "S2", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        Member tutor2 = createMember("T2@email.com", "T2", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        student = memberRepository.save(student);
        student2 = memberRepository.save(student2);
        tutor = memberRepository.save(tutor);
        tutor2 = memberRepository.save(tutor2);

        Lecture lecture1 = createLecture("L1", tutor);
        Lecture lecture2 = createLecture("L2", tutor2);
        lecture1 = lectureRepository.save(lecture1);
        lecture2 = lectureRepository.save(lecture2);

        Enrollment enrollmentTarget = createEnrollment(lecture1, student, EnrollmentStatus.WAITING, tutor);
        Enrollment enrollment1 = createEnrollment(lecture1, student, EnrollmentStatus.REJECTED, tutor);
        Enrollment enrollment2 = createEnrollment(lecture1, student2, EnrollmentStatus.WAITING, tutor);
        Enrollment enrollment3 = createEnrollment(lecture1, student2, EnrollmentStatus.REJECTED, tutor);
        Enrollment enrollment4 = createEnrollment(lecture2, student, EnrollmentStatus.WAITING, tutor2);
        Enrollment enrollment5 = createEnrollment(lecture2, student2, EnrollmentStatus.ACCEPTED, tutor2);
        enrollmentRepository.saveAll(List.of(enrollmentTarget, enrollment1, enrollment2, enrollment3, enrollment4, enrollment5));

        EnrollmentQueryFilter filter = createQueryFilter(lecture1.getId(), student.getId(), tutor.getId(),
                List.of(EnrollmentStatus.WAITING, EnrollmentStatus.ACCEPTED), null);

        // when
        Optional<Enrollment> foundedEnrollment = enrollmentRepository.findEnrollment(filter);

        // then
        assertThat(foundedEnrollment).isNotNull();
        assertThat(foundedEnrollment.get().getMember().getId()).isEqualTo(student.getId());
        assertThat(foundedEnrollment.get().getLecture().getId()).isEqualTo(lecture1.getId());
        assertThat(foundedEnrollment.get().getStatus()).isEqualTo(EnrollmentStatus.WAITING);
    }

    @DisplayName("초대 단건 검색 시 ID를 통해 초대를 정확히 한 개 조회할 수 있다.")
    @Test
    void findEnrollmentByID(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        memberRepository.saveAll(List.of(student, tutor));

        Lecture lecture1 = createLecture("L1", tutor);
        lectureRepository.save(lecture1);

        Enrollment enrollmentTarget = createEnrollment(lecture1, student, EnrollmentStatus.REJECTED, tutor);
        Enrollment enrollment = createEnrollment(lecture1, student, EnrollmentStatus.WAITING, tutor);
        enrollmentTarget = enrollmentRepository.save(enrollmentTarget);
        enrollment = enrollmentRepository.save(enrollment);

        EnrollmentQueryFilter filter = createQueryFilter(
                null, null, null, null, List.of(enrollmentTarget.getId()));

        // when
        Optional<Enrollment> foundedEnrollment = enrollmentRepository.findEnrollment(filter);

        // then
        assertThat(foundedEnrollment).isNotNull();
        assertThat(foundedEnrollment.get().getId()).isEqualTo(enrollmentTarget.getId());
        assertThat(foundedEnrollment.get().getStatus()).isEqualTo(EnrollmentStatus.REJECTED);
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