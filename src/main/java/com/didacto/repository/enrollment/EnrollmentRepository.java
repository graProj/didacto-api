package com.didacto.repository.enrollment;


import com.didacto.domain.Enrollment;
import com.didacto.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long>, EnrollmentCustomRepository {

}
