package com.didacto.common.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtil {

    // birthDate는 "yyyyMMdd" 형식의 문자열
    public OffsetDateTime parseBirth(String birth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate birthDate = LocalDate.parse(birth, formatter);
        return birthDate.atStartOfDay().atOffset(ZoneOffset.UTC);
    }
}