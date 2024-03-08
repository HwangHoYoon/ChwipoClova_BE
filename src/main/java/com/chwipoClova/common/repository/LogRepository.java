package com.chwipoClova.common.repository;

import com.chwipoClova.common.entity.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

public interface LogRepository extends JpaRepository<ApiLog, Long> {

    @Procedure("API_LOG_SAVE")
    void apiLogSave(Long userId, String apiUrl, String reqData, String resData, String message);

    @Procedure("LOGIN_LOG_SAVE")
    void loginLogSave(Long userId, String message);

    @Procedure("ACTIVITY_LOG_SAVE")
    void activityLogSave(Long userId, Integer logType, String message);
}
