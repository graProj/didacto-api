package com.didacto.service.enrollment;


import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.*;
import com.didacto.dto.enrollment.EnrollmentBasicResponse;
import com.didacto.dto.enrollment.LectureAndMemberType;
import com.didacto.repository.enrollment.EnrollmentRepository;
import com.didacto.repository.lecture.LectureRepository;
import com.didacto.repository.lectureMemer.LectureMemberRepository;
import com.didacto.repository.member.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentQueryService {
    private final EnrollmentRepository enrollmentRepository;
    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;
    private final LectureMemberRepository lectureMemberRepository;


    /**
     * [공통 : 해당 PK로 초대 정보 조회]
     * 해당 ID에 해당하는 초대 정보를 조회한다.
     * @param enrollmentId - 강의 ID
     * @return EnrollmentBasicTypeResponse
     */
    public EnrollmentBasicResponse getEnrollmentById(Long enrollmentId){

       Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(() -> {
           throw new NoSuchElementFoundException404(ErrorDefineCode.NOT_FOUND_ENROLL);
       });

        // Insert : 데이터베이스 저장
        EnrollmentBasicResponse response = new EnrollmentBasicResponse(
                enrollment.getId(),
                enrollment.getStatus(),
                enrollment.getLecture().getId(),
                enrollment.getMember().getId());


        // Out
       return response;
    }

}
