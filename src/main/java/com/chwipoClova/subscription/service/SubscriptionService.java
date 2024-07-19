package com.chwipoClova.subscription.service;

import com.chwipoClova.article.entity.Feed;
import com.chwipoClova.article.repository.FeedCustomRepository;
import com.chwipoClova.article.repository.FeedRepository;
import com.chwipoClova.common.exception.CommonException;
import com.chwipoClova.common.exception.ExceptionCode;
import com.chwipoClova.common.response.CommonResponse;
import com.chwipoClova.common.response.MessageCode;
import com.chwipoClova.common.utils.DateUtils;
import com.chwipoClova.subscription.entity.Subscription;
import com.chwipoClova.subscription.repository.SubscriptionRepository;
import com.chwipoClova.subscription.request.SubscriptionReq;
import com.chwipoClova.subscription.response.SubscriptionRes;
import com.chwipoClova.user.entity.User;
import com.chwipoClova.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final UserRepository userRepository;

    private final FeedCustomRepository feedCustomRepository;

    private final FeedRepository feedRepository;

    private final EmailService emailService;

    public CommonResponse subscription(SubscriptionReq subscriptionReq) {
        Long userId = subscriptionReq.getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new CommonException(ExceptionCode.USER_NULL.getMessage(), ExceptionCode.USER_NULL.getCode()));
        Subscription subscription = new Subscription();
        subscription.setName(user.getName());
        subscription.setEmail(user.getEmail());
        subscription.setThumbnail(user.getThumbnailImage());
        subscription.setDivision("K");
        subscription.setPublished(Instant.now());
        subscription.setDelFlag(0);
        subscription.setUser(user);
        subscriptionRepository.save(subscription);
        return new CommonResponse<>(MessageCode.OK.getCode(), null, MessageCode.OK.getMessage());
    }

    public CommonResponse subscriptionCancel(SubscriptionReq subscriptionReq) {
        Long userId = subscriptionReq.getUserId();
        Subscription subscription = subscriptionRepository.findByUser_UserId(userId).orElseThrow(() -> new CommonException(ExceptionCode.SUBSCRIPTION_NULL.getMessage(), ExceptionCode.SUBSCRIPTION_NULL.getCode()));
        subscriptionRepository.deleteById(subscription.getId());
        return new CommonResponse<>(MessageCode.OK.getCode(), null, MessageCode.OK.getMessage());
    }

    public CommonResponse test(String email) {
        LocalDate localDate = LocalDate.now();
        // 지난주 목요일
        Instant lastThurDay = DateUtils.getLocalDateToInstant(DateUtils.calculateLastWeek(localDate, DayOfWeek.THURSDAY));

        // 이번주 수요일
        Instant thisWednesDay = DateUtils.getLocalDateToInstant(DateUtils.calculateThisWeek(localDate, DayOfWeek.WEDNESDAY));

        // 지난주 목요일 이번주 수요일
        List<Feed> feedList = feedRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(lastThurDay, thisWednesDay);

        emailService.sendLoginLink(email, feedList);
        return new CommonResponse<>(MessageCode.OK.getCode(), null, MessageCode.OK.getMessage());
    }

    public CommonResponse subscriptionCheck(Long userId) {
        SubscriptionRes subscriptionRes = new SubscriptionRes();
        Optional<Subscription> subscription = subscriptionRepository.findByUser_UserId(userId);

        if (subscription.isPresent()) {
            subscriptionRes.setCheck(true);
        } else {
            subscriptionRes.setCheck(false);
        }
        return new CommonResponse<>(MessageCode.OK.getCode(), subscriptionRes, MessageCode.OK.getMessage());
    }
}
