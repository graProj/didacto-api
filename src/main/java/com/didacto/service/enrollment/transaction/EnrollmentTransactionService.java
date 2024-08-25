package com.didacto.service.enrollment.transaction;

import com.didacto.domain.Enrollment;
import com.didacto.domain.EnrollmentStatus;
import com.didacto.domain.Member;
import com.didacto.repository.enrollment.EnrollmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class EnrollmentTransactionService {

    private final EnrollmentRepository enrollmentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateCancelStatusBeforeException(Enrollment enrollment, Member tutor) {
        enrollment.updateStatus(EnrollmentStatus.CANCELLED);
        enrollment.updateModifiedMember(tutor);
        enrollmentRepository.save(enrollment);
    }
}
