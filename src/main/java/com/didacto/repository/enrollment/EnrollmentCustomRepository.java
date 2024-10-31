package com.didacto.repository.enrollment;

import com.didacto.domain.Enrollment;
import com.didacto.dto.enrollment.EnrollmentQueryFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EnrollmentCustomRepository {
    Optional<Enrollment> findEnrollment(EnrollmentQueryFilter request);
    List<Enrollment> findEnrollmentPage(Pageable pageable, EnrollmentQueryFilter request);
    Long countEnrollments(EnrollmentQueryFilter request);
}
