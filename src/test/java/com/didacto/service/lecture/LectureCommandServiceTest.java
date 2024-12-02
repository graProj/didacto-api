package com.didacto.service.lecture;

import com.didacto.common.util.DateUtil;
import com.didacto.config.exception.custom.exception.PreconditionFailException412;
import com.didacto.domain.Authority;
import com.didacto.domain.Grade;
import com.didacto.domain.Member;
import com.didacto.dto.lecture.LectureCreationRequest;
import com.didacto.dto.lecture.LectureQueryFilter;
import com.didacto.repository.lecture.LectureRepository;
import com.didacto.repository.member.MemberRepository;
import com.didacto.service.member.MemberService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class LectureCommandServiceTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LectureCommandService lectureCommandService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DateUtil dateUtil;


    @BeforeEach
    public void before() {
        lectureRepository.deleteAll();
    }

    @DisplayName("등급이 Premium이고 강의 생성시에 동시의 100개의 요청이 들어오는 경우")
    @Disabled
    @Test
    public void Premium_request_100_lectureCreate() throws Exception {
        // given
        LectureCreationRequest request = LectureCreationRequest.builder()
                .title("강의 제목")
                .build();

        Member member1 = Member.builder()
                .id(1L)
                .email("gildong1@naver.com")
                .password("gildong123!@")
                .name("회원1")
                .birth(dateUtil.parseBirth("20000513"))
                .build();

        member1.premium();
        memberRepository.saveAndFlush(member1);


        LectureQueryFilter filter = LectureQueryFilter.builder()
                .owner(member1)
                .build();

        // when
        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(32);

        // 다른 스레드에서 수행이 완료될 때 까지 대기할 수 있도록 도와주는 API - 요청이 끝날때 까지 기다림
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    lectureCommandService.create(request, member1.getId());

                } finally {
                    latch.countDown();
                }
            });
        }
        System.out.println(member1.getGrade());
        latch.await(); // 다른 쓰레드에서 수행중인 작업이 완료될때까지 기다려줌

        Long countLectures = lectureRepository.countLectures(filter);

        // then
        assertThat(countLectures).isEqualTo(100);
    }


    @DisplayName("등급이 Freeteer이고 강의 생성시에 동시의 100개의 요청이 들어오는 경우")
    @Disabled
    @Test
    public void Freeteer_request_100_lectureCreate() throws Exception {
        // given
        LectureCreationRequest request = LectureCreationRequest.builder()
                .title("강의 제목")
                .build();

        Member member1 = Member.builder()
                .id(1L)
                .email("gildong1@naver.com")
                .password("gildong123!@")
                .name("회원1")
                .birth(dateUtil.parseBirth("20000513"))
                .build();
        memberRepository.saveAndFlush(member1);

        LectureQueryFilter filter = LectureQueryFilter.builder()
                .owner(member1)
                .build();

        // when
        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(32);

        // 다른 스레드에서 수행이 완료될 때 까지 대기할 수 있도록 도와주는 API - 요청이 끝날때 까지 기다림
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    lectureCommandService.create(request, member1.getId());
                } finally {
                    latch.countDown();
                }
            });
        }
        System.out.println(member1.getGrade());
        latch.await(); // 다른 쓰레드에서 수행중인 작업이 완료될때까지 기다려줌

        Long countLectures = lectureRepository.countLectures(filter);

        // then
        assertThat(countLectures).isEqualTo(3);
    }
}