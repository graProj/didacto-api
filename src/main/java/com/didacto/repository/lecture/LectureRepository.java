package com.didacto.repository.lecture;

import com.didacto.domain.Lecture;
import com.didacto.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long>, LectureCustomRepository {

}
