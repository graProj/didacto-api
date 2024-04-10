package com.didacto.service.enrollment;


import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.AlreadyExistElementException409;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.Enrollment;
import com.didacto.domain.EnrollmentStatus;
import com.didacto.domain.Lecture;
import com.didacto.domain.Member;
import com.didacto.repository.enrollment.EnrollmentRepository;
import com.didacto.service.lecture.LectureQueryService;
import com.didacto.service.member.MemberQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentCommandService {
    private final EnrollmentRepository enrollmentRepository;
    private final LectureQueryService lectureQueryService;
    private final MemberQueryService memberQueryService;

    /**
     * [학생 : 강의 등록 요청]
     * 해당 강의 등록을 교수자에게 요청한다.
     *
     * @param lectureId - 강의 ID
     * @return Long - 초대 PK
     */
    @Transactional
    public Enrollment requestEnrollment(Long lectureId, Long memberId){

        Lecture lecture = lectureQueryService.query(lectureId);
        Member member = memberQueryService.query(memberId);

        // Validate : 이미 대기중인 초대 요청이 있는 지 조회
        isHaveAlreadyWaitRequest(member, lecture);

        // Validate : 이미 강의에 소속되어 있는 지 조회
        isEnrolled(member, lecture);

        // Insert : 데이터베이스 저장
        Enrollment enrollment = Enrollment.builder()
                .status(EnrollmentStatus.WAITING)
                .lecture(lecture)
                .member(member)
                .modified_by(member)
                .build();
        enrollment = enrollmentRepository.save(enrollment);

        // Out
       return enrollment;
    }

    /**
     * [학생 : 강의 등록 요청 취소]
     * 등록 요청을 취소한다.
     *
     * @param enrollId - 초대 ID
     * @param memberId - 멤버 ID
     * @return Long - 초대 PK
     */
    @Transactional
    public Enrollment cancelEnrollment(Long enrollId, Long memberId){

        Member member = memberQueryService.query(memberId);

        // Validate & Find : 멤버와 일치, WAITING 상태, enrollId에 해당하는 레코드 조회
        Enrollment enrollment = enrollmentRepository.findWaitingEnrollment(enrollId, memberId);
        if(enrollment == null){
            throw new NoSuchElementFoundException404(ErrorDefineCode.ALREADY_ENROLL);
        }

        // Update : Status 변경, 수정자 변경
        enrollment.updateStatus(EnrollmentStatus.CANCELLED);
        enrollment.updateModifiedMember(member);
        enrollment = enrollmentRepository.save(enrollment);

        return enrollment;
    }


    /**
     * [교수자 : 강의 등록 요청 처리 ]
     * 등록 요청에 대해서 승인, 혹은 거절한다
     *
     * @param enrollId - 초대 ID
     * @param tutorId  - 현재 사용자(교수) ID
     * @param action   - 승인/거절
     * @return Long - 초대 PK
     */
    @Transactional
    public Enrollment confirmEnrollment(Long enrollId, Long tutorId, EnrollmentStatus action){

        Member tutor = memberQueryService.query(tutorId);

        // Validate & Find : 강의 소유자와 일치, WAITING 상태, enrollId에 해당하는 레코드 조회
        Enrollment enrollment = enrollmentRepository.findWaitingEnrollmentByTutorId(enrollId, tutorId);
        if(enrollment == null){
            throw new NoSuchElementFoundException404(ErrorDefineCode.ALREADY_ENROLL);
        }

        // Update : Status 변경, 수정자 변경
        enrollment.updateStatus(action);
        enrollment.updateModifiedMember(tutor);
        enrollment = enrollmentRepository.save(enrollment);

        // 등록 승인 시 : Member <-> Lecture 연관 설정
        if(action.equals(EnrollmentStatus.ACCEPTED)){
            Member member = enrollment.getMember();
            Lecture lecture = enrollment.getLecture();

            // Validate : 이미 강의에 등록됐는지 검사
            isEnrolled(member, lecture);

            // TODO: LectureMember 생성
        }

       return enrollment;
    }


    /**
     * 해당 회원이 이미 Wait 상태인 Member-Lecture Enroll이 존재하는지 확인
     */
    private void isHaveAlreadyWaitRequest(Member member, Lecture lecture){
        boolean exist = enrollmentRepository.existWaitingEnrollmentByMemberId(member.getId(), lecture.getId());

        if(exist) {
            throw new AlreadyExistElementException409(ErrorDefineCode.ALREADY_ENROLL_REQUEST);
        }
    }



    /**
     * 이미 해당 Lecture에 Member가 등록되어 있는지 확인
     */
    private void isEnrolled(Member member, Lecture lecture){
        boolean isEnrolled = enrollmentRepository.existJoinByMemberAndLecture(
                member.getId(), lecture.getId());

        if(isEnrolled) {
            throw new AlreadyExistElementException409(ErrorDefineCode.ALREADY_JOIN);
        }
    }
}
