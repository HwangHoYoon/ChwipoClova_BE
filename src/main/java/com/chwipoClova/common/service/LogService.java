package com.chwipoClova.common.service;

import com.chwipoClova.common.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;

    public void apiLogSave(Long userId, String apiUrl, String reqData, String resData, String message) {
        logRepository.apiLogSave(userId, apiUrl, reqData, resData, message);
    }

    public void activityLogSave(Long userId, Integer type, String message) {
        logRepository.activityLogSave(userId, type, message);
    }

    public void newUserLogSave(Long userId, String message) {
        logRepository.activityLogSave(userId, 1, message);
    }

    public void loginUserLogSave(Long userId, String message) {
        logRepository.activityLogSave(userId, 2, message);
    }

    public void refreshUserLogSave(Long userId, String message) {
        logRepository.activityLogSave(userId, 3, message);
    }

    public void newResumeUserLogSave(Long userId, String message) {
        logRepository.activityLogSave(userId, 4, message);
    }

}
