package com.didacto.dto.enrollment;


import com.didacto.domain.Lecture;
import com.didacto.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentLectureAndMember {
    private Lecture lecture;
    private Member member;

}
