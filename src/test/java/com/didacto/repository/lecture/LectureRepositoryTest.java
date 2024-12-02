package com.didacto.repository.lecture;

import com.didacto.domain.Authority;
import com.didacto.domain.Lecture;
import com.didacto.domain.LectureState;
import com.didacto.domain.Member;
import com.didacto.dto.lecture.LectureQueryFilter;
import com.didacto.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class LectureRepositoryTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("페이지 크기가 3이라면 4번째 요소는 1페이지에서 조회되지 않는다.")
    public void findLecturePage1() {
        // given
        Member tutor = Member.builder()
                .email("abc123@naver.com")
                .password("1234")
                .name("김교수")
                .role(Authority.ROLE_ADMIN)
                .build();
        memberRepository.save(tutor);
        Lecture lecture1 = createLecture(tutor, "알고리즘 기초");
        Lecture lecture2 = createLecture(tutor, "알고리즘 심화");
        Lecture lecture3 = createLecture(tutor, "자료구조 기초");
        Lecture lecture4 = createLecture(tutor, "자료구조 심화");
        lectureRepository.saveAll(List.of(lecture1, lecture2, lecture3, lecture4));

        // when
        List<Lecture> lecturePage = lectureRepository.findLecturePage(
                PageRequest.of(1, 3, Sort.by(Sort.Direction.ASC, "id")),
                LectureQueryFilter.builder().build()
        );

        // then
        assertThat(lecturePage).isNotIn(lecture4);
    }

    @Test
    @DisplayName("페이지 크기가 3이라면 4번째 요소는 2페이지에서 조회된다.")
    public void findLecturePage2() {
        // given
        Member tutor = Member.builder()
                .email("abc123@naver.com")
                .password("1234")
                .name("김교수")
                .role(Authority.ROLE_ADMIN)
                .build();
        memberRepository.save(tutor);
        Lecture lecture1 = createLecture(tutor, "알고리즘 기초");
        Lecture lecture2 = createLecture(tutor, "알고리즘 심화");
        Lecture lecture3 = createLecture(tutor, "자료구조 기초");
        Lecture lecture4 = createLecture(tutor, "자료구조 심화");
        lectureRepository.saveAll(List.of(lecture1, lecture2, lecture3, lecture4));

        // when
        List<Lecture> lecturePage = lectureRepository.findLecturePage(
                PageRequest.of(2, 3, Sort.by(Sort.Direction.ASC, "id")),
                LectureQueryFilter.builder().build()
        );

        // then
        assertThat(lecturePage.stream().map(Lecture::getId)).contains(lecture4.getId());
    }

    @Test
    @DisplayName("페이지 정렬의 기본 정렬은 id의 내림차순이다.")
    public void findLecturePage3() {
        // given
        Member tutor = Member.builder()
                .email("abc123@naver.com")
                .password("1234")
                .name("김교수")
                .role(Authority.ROLE_ADMIN)
                .build();
        memberRepository.save(tutor);
        Lecture lecture1 = createLecture(tutor, "알고리즘 기초");
        Lecture lecture2 = createLecture(tutor, "알고리즘 심화");
        Lecture lecture3 = createLecture(tutor, "자료구조 기초");
        Lecture lecture4 = createLecture(tutor, "자료구조 심화");
        lectureRepository.saveAll(List.of(lecture1, lecture2, lecture3, lecture4));

        // when
        List<Lecture> lecturePage = lectureRepository.findLecturePage(
                PageRequest.of(1, 10),
                LectureQueryFilter.builder().build()
        );

        // then
        assertThat(lecturePage.get(1).getId()).isEqualTo(lecture2.getId());
    }

    @Test
    @DisplayName("알고리즘 키워드가 포함된 강의의 수를 조회한다.")
    public void countLectures() {
        // given
        Member tutor = Member.builder()
                .email("abc123@naver.com")
                .password("1234")
                .name("김교수")
                .role(Authority.ROLE_ADMIN)
                .build();
        memberRepository.save(tutor);
        Lecture lecture1 = createLecture(tutor, "알고리즘 기초");
        Lecture lecture2 = createLecture(tutor, "알고리즘 심화");
        Lecture lecture3 = createLecture(tutor, "자료구조 기초");
        Lecture lecture4 = createLecture(tutor, "자료구조 심화");
        lectureRepository.saveAll(List.of(lecture1, lecture2, lecture3, lecture4));

        // when
        Long countLectures = lectureRepository.countLectures(LectureQueryFilter.builder()
                .titleKeyword("알고리즘")
                .build()
        );

        // then
        assertThat(countLectures).isEqualTo(2);
    }

    private static Lecture createLecture(Member tutor, String lectureName) {
        return Lecture.builder()
                .title(lectureName)
                .owner(tutor)
                .state(LectureState.WAITING)
                .build();
    }
}