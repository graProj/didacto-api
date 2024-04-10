package com.didacto.repository.lecture;

import com.didacto.domain.QLectureMember;
import com.didacto.dto.lecture.LectureResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class LectureCustomRepositoryImpl implements LectureCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<LectureResponse> findBoardAllByLectureNo(long LectureNo) {
        //    public List<LectureResponse> findBoardAllByLectureNo(long LectureNo) {
//        List<Lecture> lectures = queryFactory
//                .selectFrom(lecture)
//                .where(lecture.category.categoryNo.eq(LectureNo))
//                .fetch();
//
//        return boards.stream().map(b -> new BoardDto(b)).collect(Collectors.toList());
        return null;
    }

    @Override
    public void deleteByLectureIdAndMemberId(Long lectureId, Long memberId) {
        QLectureMember qLectureMember = QLectureMember.lectureMember;

        queryFactory.update(qLectureMember)
                .where(qLectureMember.lecture.id.eq(lectureId)
                        .and(qLectureMember.member.id.eq(memberId)))
                .set(qLectureMember.deleted, true)
                .execute();
    }



}
