package com.didacto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;
@SpringBootTest
    @AutoConfigureTestDatabase
    public class H2Test {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testH2DatabaseConnection() {
        // H2 데이터베이스에 접근하는 쿼리를 실행합니다.
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM member", Integer.class);

        assertTrue(count >= 0);
    }
}


