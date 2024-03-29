package com.didacto.dto.auth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponse {
    @Schema(description = "Access Token", example = "MY_ACCESS_TOKEN")
    private String accessToken;
    @Schema(description = "Refresh Token", example = "MY_REFRESH_TOKEN")
    private String refreshToken;
}

