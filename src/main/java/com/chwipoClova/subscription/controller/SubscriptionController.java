package com.chwipoClova.subscription.controller;

import com.chwipoClova.common.response.CommonResponse;
import com.chwipoClova.subscription.request.SubscriptionReq;
import com.chwipoClova.subscription.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "subscription", description = "구독 API")
@RequestMapping("subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Operation(summary = "구독하기", description = "구독하기")
    @PostMapping(path = "/subscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    }
    )
    public CommonResponse subscription(@RequestBody SubscriptionReq subscriptionReq) throws Exception {
        return subscriptionService.subscription(subscriptionReq);
    }

    @Operation(summary = "구독취소", description = "구독취소")
    @DeleteMapping(path = "/subscriptionCancel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    }
    )
    public CommonResponse subscriptionCancel(@RequestBody SubscriptionReq subscriptionReq) throws Exception {
        return subscriptionService.subscriptionCancel(subscriptionReq);
    }

    @Operation(summary = "이메일 발송 테스트", description = "이메일 발송 테스트")
    @GetMapping(path = "/emailTest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    }
    )
    public CommonResponse emailTest(String email) {
        return subscriptionService.test(email);
    }
}
