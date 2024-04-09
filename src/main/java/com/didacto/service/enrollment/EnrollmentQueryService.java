package com.didacto.service.enrollment;


import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.*;
import com.didacto.dto.enrollment.*;
import com.didacto.repository.enrollment.EnrollmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentQueryService {
    private final EnrollmentRepository enrollmentRepository;


    /**
     * [공통 : 해당 PK로 초대 정보 조회]
     * 해당 ID에 해당하는 초대 정보를 조회한다.
     * @param enrollmentId - 강의 ID
     * @return EnrollmentBasicTypeResponse
     */
    public EnrollmentBasicResponse getEnrollmentById(Long enrollmentId){

        // Query
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(() -> {
           throw new NoSuchElementFoundException404(ErrorDefineCode.NOT_FOUND_ENROLL);
        });


        // Out
        EnrollmentBasicResponse response = EnrollmentBasicResponse.builder()
                .id(enrollment.getId())
                .status(enrollment.getStatus())
                .lecture_id(enrollment.getLecture().getId())
                .member_id(enrollment.getMember().getId())
                .member_email(enrollment.getMember().getEmail())
                .member_name(enrollment.getMember().getName())
                .createdTime(enrollment.getCreatedTime())
                .modifiedTime(enrollment.getModifiedTime())
                .build();

        return response;
    }

    /**
     * [초대 정보 리스트 조회]
     * 해당 강의에 해당하는 초대 정보를 조회한다.
     * @param lectureId - 강의 ID - 해당 강의에 해당하는 초대 정보에 대해 조회한다. Null일 시 전체 강의 조회
     * @param memberId - 학생 ID - 해당 학생이 보낸 초대 정보에 대해 조회한다. Null일 시 전체 학생 조회
     * @param condition - 페이지네이션과 필터를 포함한 조회 조건
     * @return EnrollmentListResponse
     */
    public EnrollmentListResponse getEnrollmentInfoList(Long lectureId, Long memberId, EnrollmentQueryConditionRequest condition, String order){
        long page = condition.getPage();
        long size = condition.getSize();

        // Query : Enrollments 리스트 조회 : 페이지네이션 및 조건 필터링
        List<EnrollmentBasicResponse> enrollments = enrollmentRepository.findEnrollmentsWithFilter(lectureId, memberId, condition, order);

        // Query : Pagenation을 위한 총 개수 집계
        long count = enrollmentRepository.countEnrollmentsWithFilter(lectureId, memberId, condition);

        // Calc : 총 페이지 수와 다음 페이지 존재 여부 계산
        long totalPage = (long) Math.ceil((double) count / size);
        boolean isHaveNext = page < totalPage;

        // Out
        PageInfoResponse pageInfo = PageInfoResponse.builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages(totalPage)
                .totalElements(count)
                .haveNext(isHaveNext)
                .build();

        EnrollmentListResponse listInfo = new EnrollmentListResponse(enrollments, pageInfo);
        return listInfo;
    }



}
