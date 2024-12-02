package com.didacto.service.lecture;

import com.didacto.domain.Authority;
import com.didacto.domain.Lecture;
import com.didacto.domain.LectureState;
import com.didacto.domain.Member;
import com.didacto.dto.lecture.LectureCreationRequest;
import com.didacto.dto.lecture.LectureModificationRequest;
import com.didacto.repository.lecture.LectureRepository;
import com.didacto.repository.member.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class LectureServiceTest {


    @Autowired
    private LectureCommandService lectureCommandService;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("교수가 강의를 생성한다.")
    public void 강의_생성() {
        // given
        Member tutor = Member.builder()
                .email("abc123@naver.com")
                .password("1234")
                .name("김교수")
                .role(Authority.ROLE_ADMIN)
                .build();
        memberRepository.save(tutor);

        // when
        Lecture createdLecture = lectureCommandService.create(
                LectureCreationRequest.builder()
                        .title("알고리즘 기초")
                        .build(),
                tutor.getId()
        );

        // then
        Lecture findLecture = lectureRepository.findById(createdLecture.getId()).get();
        assertThat(findLecture.getId()).isEqualTo(createdLecture.getId());
    }

    @Test
    @DisplayName("교수가 강의 제목을 수정한다.")
    public void 강의_수정() {
        // given
        String originalLectureName = "알고리즘 기초";
        String modifiedLectureName = "알고리즘 심화";

        Member tutor = Member.builder()
                .email("abc123@naver.com")
                .password("1234")
                .name("김교수")
                .role(Authority.ROLE_ADMIN)
                .build();
        memberRepository.save(tutor);

        Lecture lecture = Lecture.builder()
                .title(originalLectureName)
                .owner(tutor)
                .state(LectureState.WAITING)
                .build();
        lectureRepository.save(lecture);


        // when
        Lecture modifiedLecture = lectureCommandService.modify(
                LectureModificationRequest.builder()
                        .lectureId(lecture.getId())
                        .title(modifiedLectureName)
                        .build()
        );

        // then
        Lecture findLecture = lectureRepository.findById(modifiedLecture.getId()).get();
        assertThat(findLecture.getTitle()).isEqualTo(modifiedLectureName);
    }

    @Test
    @DisplayName("교수가 강의를 삭제한다.")
    public void 강의_삭제() {
        // given
        Member tutor = Member.builder()
                .email("abc123@naver.com")
                .password("1234")
                .name("김교수")
                .role(Authority.ROLE_ADMIN)
                .build();
        memberRepository.save(tutor);

        Lecture lecture = Lecture.builder()
                .title("알고리즘 기초")
                .owner(tutor)
                .state(LectureState.WAITING)
                .build();
        lectureRepository.save(lecture);

        // when
        Lecture deletedLecture = lectureCommandService.delete(lecture.getId());

        // then
        Lecture findLecture = lectureRepository.findById(deletedLecture.getId()).get();
        assertThat(findLecture.getDeleted()).isTrue();
    }
}