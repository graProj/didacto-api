package com.didacto.service.enrollment;


import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.Enrollment;
import com.didacto.domain.EnrollmentStatus;
import com.didacto.domain.Lecture;
import com.didacto.domain.Member;
import com.didacto.dto.enrollment.EnrollmentBasicTypeResponse;
import com.didacto.dto.enrollment.EnrollmentLectureAndMember;
import com.didacto.dto.enrollment.EnrollmentRequest;
import com.didacto.dto.example.ExampleRequest;
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

        // 존재하는 lecture, member 찾기
        EnrollmentLectureAndMember lm = getMemberAndLecture(memberId, request.getLectureId());


        Enrollment enrollment = Enrollment.builder()
                .status(EnrollmentStatus.WAITING)
                .lecture(lm.getLecture())
                .member(lm.getMember())
                .modified_by(lm.getMember())
                .build();

        enrollment = enrollmentRepository.save(enrollment);

        EnrollmentBasicTypeResponse response = new EnrollmentBasicTypeResponse(
                enrollment.getId(),
                enrollment.getStatus(),
                enrollment.getLecture().getId(),
                enrollment.getMember().getId());

        return response;
    }


    /**
     * 초대될 member와, lecture가 존재하는 지 확인하고 그 값들을 반환.
     * @param memberId
     * @param lectureId
     * @return EnrollmentLectureAndMember
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




}
