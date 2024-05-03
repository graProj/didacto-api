package com.didacto.repository.lecture;


import com.didacto.domain.QLectureMember;
import com.didacto.dto.lecture.LectureResponse;
import com.didacto.common.util.StringUtil;
import com.didacto.domain.Lecture;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import static com.didacto.domain.QLecture.lecture;
import static com.didacto.domain.QMember.member;


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



    public List<LectureResponse> findLecturesByKeyword(
            String order, String keyword, Long page, Long size)
    {

        long offset = (page - 1) * size; // 페이지네이션 Offset 계산

        // 동적 쿼리 : 정렬
        OrderSpecifier<?> orderSpecifier;
        if(StringUtil.isNullOrEmpty(order)){
            orderSpecifier = lecture.createdTime.desc();
        }
        else if (order.equals("title")) {
            orderSpecifier = lecture.title.asc();
        }
        else {
            orderSpecifier = lecture.createdTime.desc();
        }

        // 동적 쿼리 : 키워드 존재 시 키워드로 검색
        BooleanBuilder searchPredicate = new BooleanBuilder();
        if (!StringUtil.isNullOrEmpty(keyword)) {
            searchPredicate.and(lecture.title.like("%" + keyword +"%"));
        }


        List<Lecture> lectures = queryFactory.select(lecture)
                .from(lecture)
                .leftJoin(lecture.owner, member)
                .where(lecture.deleted.eq(false).and(searchPredicate))
                .orderBy(orderSpecifier)
                .offset(offset)
                .limit(size)
                .fetch();

        List<LectureResponse> list = lectures.stream()
                .map(lecture -> new LectureResponse(lecture))
                .toList();


        return list;
    }


    @Override
    public Long countLecturesByKeyword(String keyword){

        // 동적 쿼리 : 키워드 존재 시 키워드로 검색
        BooleanBuilder searchPredicate = new BooleanBuilder();
        if (!StringUtil.isNullOrEmpty(keyword)) {
            searchPredicate.and(lecture.title.like("%" + keyword +"%"));
        }

        long total = queryFactory.select(lecture)
                .from(lecture)
                .where(lecture.deleted.eq(false).and(searchPredicate))
                .fetchCount();

        return total;
    }

}
