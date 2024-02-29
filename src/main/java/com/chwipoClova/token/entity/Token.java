package com.chwipoClova.token.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@Schema(description = "Token VO")
@NoArgsConstructor
public class Token {
    @Id
    private String userId;
    private String refreshToken;

    public Token(String refreshToken, String userId) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }

    public Token updateToken(String token) {
        this.refreshToken = token;
        return this;
    }
}
