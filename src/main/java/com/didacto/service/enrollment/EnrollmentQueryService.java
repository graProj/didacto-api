package com.didacto.service.enrollment;


import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.Enrollment;
import com.didacto.domain.EnrollmentStatus;
import com.didacto.dto.PageInfoResponse;
import com.didacto.dto.enrollment.EnrollmentPageResponse;
import com.didacto.dto.enrollment.EnrollmentQueryFilter;
import com.didacto.dto.enrollment.EnrollmentResponse;
import com.didacto.repository.enrollment.EnrollmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentQueryService {
    private final EnrollmentRepository enrollmentRepository;


    /**
     * [공통 : 해당 PK로 초대 정보 조회]
     * 해당 ID에 해당하는 초대 정보를 조회한다.
     *
     * @param enrollmentId - 강의 ID
     * @return EnrollmentBasicTypeResponse
     */
    public EnrollmentResponse getEnrollmentById(Long enrollmentId) {

        // Query
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(() -> {
            throw new NoSuchElementFoundException404(ErrorDefineCode.NOT_FOUND_ENROLL);
        });

        return new EnrollmentResponse(enrollment);
    }

    public EnrollmentPageResponse queryPage(Pageable pageable, EnrollmentQueryFilter request) {
        long page = pageable.getPageNumber();
        long size = pageable.getPageSize();

        // Query : 페이지네이션 및 조건 필터링
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentPage(pageable, request);

        // Query : Pagenation을 위한 총 개수 집계
        long count = enrollmentRepository.countEnrollments(request);

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

        return new EnrollmentPageResponse(pageInfo, enrollments);
    }

    public Optional<Enrollment> findWaitingEnrollment(Long enrollId, Long memberId) {
        return enrollmentRepository.findEnrollment(
                EnrollmentQueryFilter.builder()
                        .ids(List.of(enrollId))
                        .memberId(memberId)
                        .statuses(List.of(EnrollmentStatus.WAITING))
                        .build()
        );
    }

    public Optional<Enrollment> findWaitingEnrollmentByTutorId(Long enrollId, Long tutorId) {
        return enrollmentRepository.findEnrollment(
                EnrollmentQueryFilter.builder()
                        .ids(List.of(enrollId))
                        .tutorId(tutorId)
                        .statuses(List.of(EnrollmentStatus.WAITING))
                        .build()
        );
    }

    public boolean existWaitingEnrollmentByMemberId(Long memberId, Long lectureId) {
        return enrollmentRepository.findEnrollment(
                EnrollmentQueryFilter.builder()
                        .memberId(memberId)
                        .lectureId(lectureId)
                        .statuses(List.of(EnrollmentStatus.WAITING))
                        .build()
        ).isPresent();
    }
}
