package com.chwipoClova.subscription.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SubscriptionRes {
    @Schema(description = "구독여부", example = "true", name = "check")
    private boolean check;
}
