package com.didacto.service.enrollment;


import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.AlreadyExistElementException409;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.Enrollment;
import com.didacto.domain.EnrollmentStatus;
import com.didacto.domain.Lecture;
import com.didacto.domain.Member;
import com.didacto.dto.enrollment.EnrollmentBasicTypeResponse;
import com.didacto.dto.enrollment.EnrollmentLectureAndMember;
import com.didacto.dto.enrollment.EnrollmentRequest;
import com.didacto.repository.enrollment.EnrollmentRepository;
import com.didacto.repository.lecture.LectureRepository;
import com.didacto.repository.member.MemberRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;


    /**
     * [학생 : 강의 등록 요청]
     * 해당 강의 등록을 교수자에게 요청한다.
     * @param EnrollmentRequest - .
     * @return Long - 생성된 Enrollment PK
     */
    @Transactional
    public EnrollmentBasicTypeResponse requestEnrollment(EnrollmentRequest request, Long memberId){

        // Validate : lecture, member의 존재여부 확인
        EnrollmentLectureAndMember targets = getMemberAndLecture(memberId, request.getLectureId());

        // Validate : 이미 대기중인 초대 요청이 있는 지 조회
        isHaveAlreadyWaitRequest(targets.getMember());

        // Validate : 이미 강의에 소속되어 있는 지 조회
        isHaveAlreadyJoin(targets.getMember(), targets.getLecture());

        // Insert : 데이터베이스 저장
        Enrollment enrollment = Enrollment.builder()
                .status(EnrollmentStatus.WAITING)
                .lecture(targets.getLecture())
                .member(targets.getMember())
                .modified_by(targets.getMember())
                .build();
        enrollment = enrollmentRepository.save(enrollment);

        // Out : Object 변환 후 반환
        EnrollmentBasicTypeResponse response = new EnrollmentBasicTypeResponse(
                enrollment.getId(),
                enrollment.getStatus(),
                enrollment.getLecture().getId(),
                enrollment.getMember().getId());

        return response;
    }


    /**
     * 초대될 member와, lecture가 존재하는 지 확인하고 그 값들을 반환.
     */
    private EnrollmentLectureAndMember getMemberAndLecture(Long memberId, Long lectureId){

        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> {
            throw new NoSuchElementFoundException404(ErrorDefineCode.LECTURE_NOT_FOUND);
        });

        Member member = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new NoSuchElementFoundException404(ErrorDefineCode.USER_NOT_FOUND);
        });

        return new EnrollmentLectureAndMember(lecture, member);
    }


    /**
     * 이미 Wait 상태인 Member-Lecture Enroll이 존재하는지 확인
     */
    private void isHaveAlreadyWaitRequest(Member member){
        boolean exist = enrollmentRepository.isHaveWaitingEnrollmentByMemberId(member.getId());

        if(exist) {
            throw new AlreadyExistElementException409(ErrorDefineCode.ALREADY_ENROLL_REQUEST);
        }
    }

    /**
     * 이미 Wait 상태인 Member-Lecture Enroll이 존재하는지 확인
     */
    private void isHaveAlreadyJoin(Member member, Lecture lecture){
        boolean isJoined = enrollmentRepository.alreadyJoinedCheck(
                member.getId(), lecture.getId());

        if(isJoined) {
            throw new AlreadyExistElementException409(ErrorDefineCode.ALREADY_JOIN);
        }
    }




}
