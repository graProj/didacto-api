package com.didacto.common;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class ExpirationPeriod {
    public static final OffsetDateTime PREMIUM_EXPIRATION_DATE;

    static {
        // 특정 날짜를 기준으로 설정 (여기서는 1년 후)
        PREMIUM_EXPIRATION_DATE = OffsetDateTime.now().plusYears(1).withOffsetSameInstant(ZoneOffset.UTC);
    }
}
