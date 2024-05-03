package com.didacto.repository.lecturemember;

import com.didacto.domain.LectureMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface LectureMemberRepository extends JpaRepository<LectureMember, Long>, LectureMemberCustomRepository {
}
