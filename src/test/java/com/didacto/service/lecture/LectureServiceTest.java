package com.didacto.service.lecture;

import com.didacto.domain.Authority;
import com.didacto.domain.Lecture;
import com.didacto.domain.LectureState;
import com.didacto.domain.Member;
import com.didacto.dto.lecture.LectureCreationRequest;
import com.didacto.dto.lecture.LectureModificationRequest;
import com.didacto.dto.lecture.LecturePageResponse;
import com.didacto.dto.lecture.LectureQueryFilter;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@DisplayName("Lecture Service")
@AutoConfigureTestDatabase
public class LectureServiceTest {

    @Autowired
    private LectureQueryService lectureQueryService;

    @Autowired
    private LectureCommandService lectureCommandService;

    @Autowired
    private EntityManager em;

    private Long tutorId;
    private Long lecturId;

    @BeforeEach
    public void before(){
        this.initializeData();
    }

    @AfterEach
    public void after(){}

    @Test
    @DisplayName("Lecture : (교수) 강의 생성")
    public void 강의_생성() {
        // given
        LectureCreationRequest request = LectureCreationRequest.builder()
                .title("강의 제목")
                .build();

        // when
        Lecture lecture = lectureCommandService.create(request, tutorId);

        // then
        assertThat(lecture).isNotNull();
    }

    @Test
    @DisplayName("Lecture : (교수) 강의 수정")
    public void 강의_수정() {
        // given
        String modifiedName = "수정된 강의 제목";
        LectureModificationRequest request = LectureModificationRequest.builder()
                .lectureId(lecturId)
                .title(modifiedName)
                .build();

        // when
        Lecture modifiedLecture = lectureCommandService.modify(request);

        // then
        assertEquals(modifiedName, modifiedLecture.getTitle(), "강의 제목이 수정되어야 합니다.");
    }

    @Test
    @DisplayName("Lecture : (교수) 강의 삭제")
    public void 강의_삭제() {
        // when
        Lecture deletedLecture = lectureCommandService.delete(lecturId);

        // then
        assertEquals(true, deletedLecture.getDeleted(), "강의가 삭제되어야 합니다.");
    }

    @Test
    @DisplayName("Lecture : (교수) 강의 조회")
    public void 강의_단건_조회() {
        // given, when
        Lecture lecture = lectureQueryService.queryOne(lecturId);

        // then
        assertThat(lecture).isNotNull();
    }

    @Test
    @DisplayName("Lecture : 키워드로 강의 리스트 조회")
    public void 강의_키워드로_리스트_조회() {
        // given, when
        LecturePageResponse lectures = lectureQueryService.queryPage(
                PageRequest.of(1, 10),
                LectureQueryFilter.builder()
                        .titleKeyword("TestCode")
                        .build()
        );

        // then
        assertThat(lectures.getLectures().size()).isEqualTo(2); //키워드 검색 검증
        assertThat(lectures.getPageInfo().getHaveNext()).isEqualTo(false); //페이지네이션 검증
        assertThat(lectures.getPageInfo().getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("Lecture : 강의 전체 리스트 조회")
    public void 강의_전체_리스트_조회() {
        // given, when
        LecturePageResponse lectures = lectureQueryService.queryPage(
                PageRequest.of(2, 2, Sort.by(Sort.Direction.DESC,"id")),
                LectureQueryFilter.builder().build()
        );

        // then
        assertThat(lectures.getLectures())
                .extracting(e -> e.getId())
                .contains(lecturId);    //정렬 검증


    }



    private void initializeData(){
        Member tutor = Member.builder()
                .email("enrolltec@test.com")
                .password("test123!")
                .name("교수1")
                .role(Authority.ROLE_ADMIN)
                .birth(OffsetDateTime.now())
                .build();

        Lecture lecture = Lecture.builder()
                .title("강의1")
                .owner(tutor)
                .state(LectureState.WAITING)
                .build();

        Lecture lecture2 = Lecture.builder()
                .title("TestCodeLecture1")
                .owner(tutor)
                .state(LectureState.WAITING)
                .build();

        Lecture lecture3 = Lecture.builder()
                .title("TestCodeLecture2")
                .owner(tutor)
                .state(LectureState.WAITING)
                .build();

        em.persist(tutor);
        em.persist(lecture);
        em.persist(lecture2);
        em.persist(lecture3);
        em.clear();
        em.flush();

        tutorId = tutor.getId();
        lecturId = lecture.getId();
    }
}
