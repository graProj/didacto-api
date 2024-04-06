package com.didacto.repository.lectureMemer;

import com.didacto.domain.Lecture;
import com.didacto.domain.LectureMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureMemberRepository extends JpaRepository<LectureMember, Long> {
}
