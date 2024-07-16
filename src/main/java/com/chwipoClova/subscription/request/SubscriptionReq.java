package com.chwipoClova.subscription.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SubscriptionReq {
    @Schema(description = "유저 ID", example = "1", name = "userId")
    private Long userId;
}
