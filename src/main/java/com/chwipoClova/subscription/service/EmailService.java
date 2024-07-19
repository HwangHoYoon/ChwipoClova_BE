package com.chwipoClova.subscription.service;

import com.chwipoClova.article.entity.Feed;
import com.chwipoClova.common.utils.DateUtils;
import com.chwipoClova.subscription.request.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine htmlTemplateEngine;
    private static final String EXAMPLE_LINK_TEMPLATE = "mail/mailTemplate";

    @Value("${domain}")
    private String domain;

    public void send(EmailMessage emailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            /**
             * 첨부 파일(Multipartfile) 보낼거면 true
             */
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());

            /**
             * html 템플릿으로 보낼거면 true
             * plaintext로 보낼거면 false
             */
            mimeMessageHelper.setText(emailMessage.getMessage(), true);
            javaMailSender.send(mimeMessage);
            log.info("sent email: {}", emailMessage.getMessage());
        } catch (MessagingException e) {
            log.error("[EmailService.send()] error {}", e.getMessage());
        }
    }

    public void sendLoginLink(String email, List<Feed> feedList) {
        Context context = getContext(feedList);
        String message = htmlTemplateEngine.process(EXAMPLE_LINK_TEMPLATE, context);

        LocalDate localDate = LocalDate.now();

        int weekOfMonth = DateUtils.getWeekOfMonth(localDate);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(email)
                .subject("[티키타카] " + localDate.getMonth().getValue() + "월 " + weekOfMonth + "주차 커리어 뉴스")
                .message(message)
                .build();
        send(emailMessage);
    }

    private Context getContext(List<Feed> feedList) {
        Context context = new Context();
        // 상위 10개만 뽑음
        List<Feed> feeds = new ArrayList<>();
        if (feedList.size() > 10) {
            for (int i = 0; i < 10; i++) {
                feeds.add(feedList.get(i));
            }
        } else {
            feeds = feedList;
        }
        context.setVariable("items", feeds);
        context.setVariable("count", "총 " + feedList.size() + "개");
        context.setVariable("image", domain + "/mail/tiki.png");
        return context;
    }
}