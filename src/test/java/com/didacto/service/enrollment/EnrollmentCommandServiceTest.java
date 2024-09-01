package com.didacto.service.enrollment;

import com.didacto.config.exception.custom.exception.AlreadyExistElementException409;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.*;
import com.didacto.dto.enrollment.EnrollmentQueryFilter;
import com.didacto.dto.lecturemember.LectureMemberQueryFilter;
import com.didacto.repository.enrollment.EnrollmentRepository;
import com.didacto.repository.lecture.LectureRepository;
import com.didacto.repository.lecturemember.LectureMemberRepository;
import com.didacto.repository.member.MemberRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class EnrollmentCommandServiceTest {

    @Autowired
    private EnrollmentCommandService enrollmentCommandService;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private LectureMemberRepository lectureMemberRepository;

    @DisplayName("특정 학생은 특정 강의에 대해 초대 요청을 할 수 있다.")
    @Test
    void requestEnrollment(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        student = memberRepository.saveAndFlush(student);
        tutor = memberRepository.saveAndFlush(tutor);

        Lecture lecture = createLecture("L1", tutor);
        lecture = lectureRepository.saveAndFlush(lecture);

        // when
        Long requestEnrollmentId = enrollmentCommandService.requestEnrollment(lecture.getId(), student.getId());

        // then
        Optional<Enrollment> requestEnrollment = enrollmentRepository.findEnrollment(createQueryFilter(
                null, null, null, null, List.of(requestEnrollmentId)));
        assertThat(requestEnrollment).isNotNull();
        assertThat(requestEnrollment.get().getMember().getName()).isEqualTo("S1");
        assertThat(requestEnrollment.get().getLecture().getTitle()).isEqualTo("L1");
        assertThat(requestEnrollment.get().getStatus()).isEqualTo(EnrollmentStatus.WAITING);

    }

    @DisplayName("여러명의 학생은 특정 강의에 대해 모두 초대 요청을 할 수 있다.")
    @Test
    void requestEnrollmentWithMultiStudent(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member student2 = createMember("S2@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        student = memberRepository.saveAndFlush(student);
        student2 = memberRepository.saveAndFlush(student2);
        tutor = memberRepository.saveAndFlush(tutor);

        Lecture lecture = createLecture("L1", tutor);
        lecture = lectureRepository.saveAndFlush(lecture);

        // when
        Long requestEnrollmentId = enrollmentCommandService.requestEnrollment(lecture.getId(), student.getId());
        Long requestEnrollmentId2 = enrollmentCommandService.requestEnrollment(lecture.getId(), student2.getId());

        // then
        Pageable page = createPagable();
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentPage(
                createPagable(), createQueryFilter(
                lecture.getId(), null, null, null, null));

        assertThat(enrollments).hasSize(2)
                .extracting("id", "member.id", "lecture.id", "status")
                .containsExactlyInAnyOrder(
                        tuple(requestEnrollmentId, student.getId(), lecture.getId(), EnrollmentStatus.WAITING),
                        tuple(requestEnrollmentId2, student2.getId(), lecture.getId(), EnrollmentStatus.WAITING)
                );

    }

    @DisplayName("특정 학생은 여러개의 강의에 대해 모두 초대 요청을 할 수 있다.")
    @Test
    void requestEnrollmentWithMultiLecture(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        student = memberRepository.saveAndFlush(student);
        tutor = memberRepository.saveAndFlush(tutor);

        Lecture lecture = createLecture("L1", tutor);
        Lecture lecture2 = createLecture("L2", tutor);
        lecture = lectureRepository.saveAndFlush(lecture);
        lecture2 = lectureRepository.saveAndFlush(lecture2);

        // when
        Long requestEnrollmentId = enrollmentCommandService.requestEnrollment(lecture.getId(), student.getId());
        Long requestEnrollmentId2 = enrollmentCommandService.requestEnrollment(lecture2.getId(), student.getId());

        // then
        Pageable page = createPagable();
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentPage(
                createPagable(), createQueryFilter(
                        null, student.getId(), null, null, null));

        assertThat(enrollments).hasSize(2)
                .extracting("id", "member.id", "lecture.id", "status")
                .containsExactlyInAnyOrder(
                        tuple(requestEnrollmentId, student.getId(), lecture.getId(), EnrollmentStatus.WAITING),
                        tuple(requestEnrollmentId2, student.getId(), lecture2.getId(), EnrollmentStatus.WAITING)
                );

    }

    @DisplayName("기존에 요청한 초대의 상태가 취소(CANCELLED)상태일 경우 다시 초대를 요청할 수 있다.")
    @Test
    void requestEnrollmentWhenBeforeCanceled(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        student = memberRepository.saveAndFlush(student);
        tutor = memberRepository.saveAndFlush(tutor);

        Lecture lecture = createLecture("L1", tutor);
        lecture = lectureRepository.saveAndFlush(lecture);

        Enrollment existEnroll = createEnrollment(lecture, student, EnrollmentStatus.CANCELLED, student);
        enrollmentRepository.saveAndFlush(existEnroll);

        // when
        Long requestEnrollmentId = enrollmentCommandService.requestEnrollment(lecture.getId(), student.getId());

        // then
        Optional<Enrollment> requestEnrollment = enrollmentRepository.findEnrollment(createQueryFilter(
                null, null, null, null, List.of(requestEnrollmentId)));

        assertThat(requestEnrollment).isNotNull();
        assertThat(requestEnrollment.get().getMember().getName()).isEqualTo("S1");
        assertThat(requestEnrollment.get().getLecture().getTitle()).isEqualTo("L1");
        assertThat(requestEnrollment.get().getStatus()).isEqualTo(EnrollmentStatus.WAITING);

    }

    @DisplayName("기존에 요청한 초대의 상태가 거절(REJECTED)상태일 경우 다시 초대를 요청할 수 있다.")
    @Test
    void requestEnrollmentWhenBeforeRejected(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        student = memberRepository.saveAndFlush(student);
        tutor = memberRepository.saveAndFlush(tutor);

        Lecture lecture = createLecture("L1", tutor);
        lecture = lectureRepository.saveAndFlush(lecture);

        Enrollment existEnroll = createEnrollment(lecture, student, EnrollmentStatus.REJECTED, student);
        enrollmentRepository.saveAndFlush(existEnroll);

        // when
        Long requestEnrollmentId = enrollmentCommandService.requestEnrollment(lecture.getId(), student.getId());

        // then
        Optional<Enrollment> requestEnrollment = enrollmentRepository.findEnrollment(createQueryFilter(
                null, null, null, null, List.of(requestEnrollmentId)));

        assertThat(requestEnrollment).isNotNull();
        assertThat(requestEnrollment.get().getMember().getName()).isEqualTo("S1");
        assertThat(requestEnrollment.get().getLecture().getTitle()).isEqualTo("L1");
        assertThat(requestEnrollment.get().getStatus()).isEqualTo(EnrollmentStatus.WAITING);

    }

    @DisplayName("특정 강의에 이미 초대 요청을 보내 대기 상태(WAITING)인 경우 초대를 요청할 수 없다.")
    @Test
    void requestEnrollmentWhenExistedWaiting(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        student = memberRepository.saveAndFlush(student);
        tutor = memberRepository.saveAndFlush(tutor);

        Lecture lecture = createLecture("L1", tutor);
        lecture = lectureRepository.saveAndFlush(lecture);

        Enrollment existEnroll = createEnrollment(lecture, student, EnrollmentStatus.WAITING, student);
        enrollmentRepository.saveAndFlush(existEnroll);

        // when, then
        final Long lectureId = lecture.getId();
        final Long studentId = student.getId();
        assertThatThrownBy(() -> enrollmentCommandService.requestEnrollment(lectureId, studentId))
                .isInstanceOf(AlreadyExistElementException409.class)
                .hasMessage("이미 교수자에게 강의 등록 요청을 보냈습니다.");

    }

    @DisplayName("특정 강의가 삭제된 상태인 경우 초대를 요청할 수 없다.")
    @Test
    void requestEnrollmentWhenDeletedLecture(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        student = memberRepository.saveAndFlush(student);
        tutor = memberRepository.saveAndFlush(tutor);

        Lecture lecture = createLecture("L1", tutor);
        lecture.delete();
        lecture = lectureRepository.saveAndFlush(lecture);

        // when, then
        final Long lectureId = lecture.getId();
        final Long studentId = student.getId();
        assertThatThrownBy(() -> enrollmentCommandService.requestEnrollment(lectureId, studentId))
                .isInstanceOf(NoSuchElementFoundException404.class)
                .hasMessage("삭제된 강의입니다.");

    }

    @DisplayName("이미 해당 강의에 소속된 경우 초대 요청을 보낼 수 없다.")
    @Test
    void requestEnrollmentWhenAlreadyJoin(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        student = memberRepository.saveAndFlush(student);
        tutor = memberRepository.saveAndFlush(tutor);

        Lecture lecture = createLecture("L1", tutor);
        lecture = lectureRepository.saveAndFlush(lecture);

        LectureMember lectureMember = LectureMember.builder()
                .member(student)
                .lecture(lecture)
                .modifiedBy(tutor)
                .build();

        lectureMemberRepository.saveAndFlush(lectureMember);

        // when, then
        final Long lectureId = lecture.getId();
        final Long studentId = student.getId();
        assertThatThrownBy(() -> enrollmentCommandService.requestEnrollment(lectureId, studentId))
                .isInstanceOf(AlreadyExistElementException409.class)
                .hasMessage("이미 강의에 등록된 상태입니다.");

    }


    @DisplayName("특정 학생은 대기중이 아닌(CANCELED, REJECT, ACCEPT) 초대 상태를 취소할 수 없다.")
    @ParameterizedTest
    @EnumSource(value = EnrollmentStatus.class, names = {"ACCEPTED", "REJECTED", "CANCELLED"})
    void requestCancelWhenNotWaiting(EnrollmentStatus status){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        student = memberRepository.saveAndFlush(student);
        tutor = memberRepository.saveAndFlush(tutor);

        Lecture lecture = createLecture("L1", tutor);
        lecture = lectureRepository.saveAndFlush(lecture);

        Enrollment enrollment = createEnrollment(lecture, student, status, student);
        enrollmentRepository.saveAndFlush(enrollment);


        // when, then
        final Long enrollmentId = enrollment.getId();
        final Long studentId = student.getId();
        assertThatThrownBy(() -> enrollmentCommandService.cancelEnrollment(enrollmentId, studentId))
                .isInstanceOf(NoSuchElementFoundException404.class)
                .hasMessage("등록 요청에 대한 처리가 이미 완료되었습니다. 혹은 해당 등록 처리에 대한 사용자의 권한이 없습니다.");


    }

    @DisplayName("특정 학생은 대기중(WAITING)인 초대 상태를 취소할 수 있다.")
    @Test
    void requestCancel(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        student = memberRepository.saveAndFlush(student);
        tutor = memberRepository.saveAndFlush(tutor);

        Lecture lecture = createLecture("L1", tutor);
        lecture = lectureRepository.saveAndFlush(lecture);

        Enrollment enrollment = createEnrollment(lecture, student, EnrollmentStatus.WAITING, student);
        enrollmentRepository.saveAndFlush(enrollment);

        // when
        Long requestEnrollmentId = enrollmentCommandService.cancelEnrollment(enrollment.getId(), student.getId());

        // then
        Optional<Enrollment> requestEnrollment = enrollmentRepository.findEnrollment(createQueryFilter(
                null, null, null, null, List.of(requestEnrollmentId)));

        assertThat(requestEnrollment).isNotNull();
        assertThat(requestEnrollment.get().getMember().getName()).isEqualTo("S1");
        assertThat(requestEnrollment.get().getLecture().getTitle()).isEqualTo("L1");
        assertThat(requestEnrollment.get().getStatus()).isEqualTo(EnrollmentStatus.CANCELLED);

    }

    @DisplayName("요청자가 초대를 요청한 학생과 일치하지 않으면 취소할 수 없다.")
    @Test
    void requestCancelWhenNotEqualRequestStudent(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member student2 = createMember("S2@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        student = memberRepository.saveAndFlush(student);
        student2 = memberRepository.saveAndFlush(student2);
        tutor = memberRepository.saveAndFlush(tutor);

        Lecture lecture = createLecture("L1", tutor);
        lecture = lectureRepository.saveAndFlush(lecture);

        Enrollment enrollment = createEnrollment(lecture, student, EnrollmentStatus.WAITING, student);
        enrollmentRepository.saveAndFlush(enrollment);

        // when, then
        final Long enrollmentId = enrollment.getId();
        final Long anotherStudentId = student2.getId();
        assertThatThrownBy(() -> enrollmentCommandService.cancelEnrollment(enrollmentId, anotherStudentId))
                .isInstanceOf(NoSuchElementFoundException404.class)
                .hasMessage("등록 요청에 대한 처리가 이미 완료되었습니다. 혹은 해당 등록 처리에 대한 사용자의 권한이 없습니다.");


    }

    @DisplayName("해당 초대 강의의 소유 교수자는 초대 요청를 수락 혹은 거절(ACCEPTED, REJECTED)할 수 있다.")
    @ParameterizedTest
    @EnumSource(value = EnrollmentStatus.class, names = {"ACCEPTED", "REJECTED"})
    void applyEnrollment(EnrollmentStatus status){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        student = memberRepository.saveAndFlush(student);
        tutor = memberRepository.saveAndFlush(tutor);

        Lecture lecture = createLecture("L1", tutor);
        lecture = lectureRepository.saveAndFlush(lecture);

        Enrollment enrollment = createEnrollment(lecture, student, EnrollmentStatus.WAITING, student);
        enrollmentRepository.saveAndFlush(enrollment);

        // when
        Long requestEnrollmentId = enrollmentCommandService.confirmEnrollment(enrollment.getId(), tutor.getId(), status);

        // then
        Optional<Enrollment> requestEnrollment = enrollmentRepository.findEnrollment(createQueryFilter(
                null, null, null, null, List.of(requestEnrollmentId)));

        assertThat(requestEnrollment).isNotNull();
        assertThat(requestEnrollment.get().getMember().getName()).isEqualTo("S1");
        assertThat(requestEnrollment.get().getLecture().getTitle()).isEqualTo("L1");
        assertThat(requestEnrollment.get().getStatus()).isEqualTo(status);

    }

    @DisplayName("해당 초대 강의의 소유 교수자가 아니면 초대 요청을 변경할 수 없다.")
    @ParameterizedTest
    @EnumSource(value = EnrollmentStatus.class, names = {"ACCEPTED", "REJECTED"})
    void applyEnrollmentWhenNotEqualOwner(EnrollmentStatus status){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        Member tutor2 = createMember("T2@email.com", "T2", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        student = memberRepository.saveAndFlush(student);
        tutor = memberRepository.saveAndFlush(tutor);
        tutor2 = memberRepository.saveAndFlush(tutor2);

        Lecture lecture = createLecture("L1", tutor);
        lecture = lectureRepository.saveAndFlush(lecture);

        Enrollment enrollment = createEnrollment(lecture, student, EnrollmentStatus.WAITING, student);
        enrollmentRepository.saveAndFlush(enrollment);

        // when, then
        final Long enrollmentId = enrollment.getId();
        final Long notOwerTutorId = tutor2.getId();
        assertThatThrownBy(() -> enrollmentCommandService.confirmEnrollment(enrollmentId, notOwerTutorId, status))
                .isInstanceOf(NoSuchElementFoundException404.class)
                .hasMessage("등록 요청에 대한 처리가 이미 완료되었습니다. 혹은 해당 등록 처리에 대한 사용자의 권한이 없습니다.");

    }

    @DisplayName("초대 요청이 수락(ACCEPTED)되면 해당 학생은 강의의 구성원으로 추가된다.")
    @Test
    void joinStudentInLectureWhenAcceptEnrollment(){
        // given
        Member student = createMember("S1@email.com", "S1", Grade.Freeteer, Authority.ROLE_USER, false);
        Member tutor = createMember("T1@email.com", "T1", Grade.Freeteer, Authority.ROLE_ADMIN, false);
        student = memberRepository.saveAndFlush(student);
        tutor = memberRepository.saveAndFlush(tutor);

        Lecture lecture = createLecture("L1", tutor);
        lecture = lectureRepository.saveAndFlush(lecture);

        Enrollment enrollment = createEnrollment(lecture, student, EnrollmentStatus.WAITING, student);
        enrollmentRepository.saveAndFlush(enrollment);

        // when
        Long requestEnrollmentId = enrollmentCommandService.confirmEnrollment(enrollment.getId(), tutor.getId(), EnrollmentStatus.ACCEPTED);

        // then
        Optional<LectureMember> relation = lectureMemberRepository.findLectureMember(createLecutreMemberQueryFilter(lecture.getId(), student.getId(), null));
        assertThat(relation).isNotNull();
        assertThat(relation.get().getMember().getName()).isEqualTo("S1");
        assertThat(relation.get().getLecture().getTitle()).isEqualTo("L1");
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
                .lectureMembers(new ArrayList())
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

    private LectureMemberQueryFilter createLecutreMemberQueryFilter(
            Long lectureId, Long memberId, List<Long> ids){

        return LectureMemberQueryFilter.builder()
                .lectureId(lectureId)
                .memberId(memberId)
                .memberIds(ids).build();
    }

    private Pageable createPagable(){
        return PageRequest.of(1, 100);
    }


    private OffsetDateTime getDummyDateTime(){
        return OffsetDateTime.of(2024, 8, 5, 10, 20, 0, 0, ZoneOffset.UTC);
    }


}