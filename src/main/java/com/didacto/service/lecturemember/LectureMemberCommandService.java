package com.didacto.service.lecturemember;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.AlreadyExistElementException409;
import com.didacto.config.exception.custom.exception.ForbiddenException403;
import com.didacto.domain.Enrollment;
import com.didacto.domain.Lecture;
import com.didacto.domain.LectureMember;
import com.didacto.domain.Member;
import com.didacto.dto.lecturemember.LectureMemberQueryFilter;
import com.didacto.repository.lecturemember.LectureMemberRepository;
import com.didacto.service.member.MemberQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class LectureMemberCommandService {
    private final LectureMemberRepository lectureMemberRepository;
    private final LectureMemberQueryService lectureMemberQueryService;
    private final MemberQueryService memberQueryService;

    /**
     * Enroolment 로 부터 LectureMember를 생성한다
     */
    @Transactional
    public LectureMember createLectureMember(Enrollment enrollment) {
        if (isJoined(enrollment.getLecture(), enrollment.getMember())) {
            throw new AlreadyExistElementException409(ErrorDefineCode.LECTURE_MEMBER_ALREADY_EXISTENCE);
        }

        LectureMember lectureMember = LectureMember.builder()
                .member(enrollment.getMember())
                .lecture(enrollment.getLecture())
                .modifiedBy(enrollment.getLecture().getOwner())
                .build();

        return lectureMemberRepository.save(lectureMember);
    }

    /**
     * lecture에 member가 등록됐는지 확인한다.
     */
    private boolean isJoined(Lecture lecture, Member member) {
        return lecture.getLectureMembers().stream()
                .filter(lectureMember -> !lectureMember.getDeleted())
                .map(LectureMember::getMember).toList()
                .contains(member);
    }

    /**
     * LectureMember를 삭제한다
     *
     * @param deletedBy 삭제를 요청한 memberId. 삭제할 대상의 memberId와 일치해야함.
     */
    @Transactional
    public LectureMember deleteLectureMember(Long lectureId, Long memberId, Long deletedBy) {
        LectureMember lectureMember = lectureMemberQueryService.queryOne(
                LectureMemberQueryFilter.builder()
                        .lectureId(lectureId)
                        .memberId(memberId)
                        .deleted(false)
                        .build()
        );

        if (!lectureMember.getMember().getId().equals(deletedBy)) {
            throw new ForbiddenException403(ErrorDefineCode.AUTHORIZATION_FAIL);
        }

        lectureMember.delete(memberQueryService.query(deletedBy));
        return lectureMemberRepository.save(lectureMember);
    }

    @Transactional
    public List<LectureMember> deleteLectureMembers(Long lectureId, List<Long> memberIds, Long deletedBy) {
        Member deletedByMember = memberQueryService.query(deletedBy);

        List<LectureMember> lectureMembers = lectureMemberQueryService.query(
                LectureMemberQueryFilter.builder()
                        .lectureId(lectureId)
                        .memberIds(memberIds)
                        .deleted(false)
                        .build()
        );

        lectureMembers.forEach(lectureMember -> {
            lectureMember.delete(deletedByMember);
        });

        return lectureMemberRepository.saveAll(lectureMembers);
    }
}