package com.didacto.config.security.custom;

import com.didacto.domain.Authority;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomUser {
    private Long id;

    private String email;

    private String password;

    private Authority role;

}
