package com.didacto.config.security;

import com.didacto.domain.Authority;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomUserDto{
    private Long id;

    private String email;

    private String password;

    private Authority role;

}
