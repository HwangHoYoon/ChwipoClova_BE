package com.chwipoClova.subscription.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailMessage {
    // 수신자
    private String to;

    // 제목
    private String subject;

    // 메시지
    private String message;
}
