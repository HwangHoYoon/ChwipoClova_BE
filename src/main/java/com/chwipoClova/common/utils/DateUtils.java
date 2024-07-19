package com.chwipoClova.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DateUtils {

    public static String getStringDateFormat(String asisFormat, String tobeFormat, String value) {
        return LocalDate.parse(value, DateTimeFormatter.ofPattern(asisFormat)).format(DateTimeFormatter.ofPattern(tobeFormat));
    }

    public static String getStringTimeFormat(String asisFormat, String tobeFormat, String value) {
        return LocalTime.parse(value, DateTimeFormatter.ofPattern(asisFormat)).format(DateTimeFormatter.ofPattern(tobeFormat));
    }

    public static int getWeekOfMonth(LocalDate date) {
        // 한국 로케일을 기준으로 주차를 계산합니다
        WeekFields weekFields = WeekFields.of(Locale.KOREA);

        // 현재 날짜가 속한 월의 주차를 반환합니다
        return date.get(weekFields.weekOfMonth());
    }

    // 현재 날짜를 기준으로 지난주 목요일 계산
    public static LocalDate calculateLastWeek(LocalDate date, DayOfWeek dayOfWeek) {
        // 현재 주의 목요일
        LocalDate thisWeek = calculateThisWeek(date, dayOfWeek);
        // 지난주 목요일
        return thisWeek.minusWeeks(1);
    }

    // 현재 날짜를 기준으로 이번 주 수요일 계산
    public static LocalDate calculateThisWeek(LocalDate date, DayOfWeek dayOfWeek) {
        // 현재 주의 수요일
        return date.with(TemporalAdjusters.nextOrSame(dayOfWeek));
    }

    public static Instant getLocalDateToInstant(LocalDate localDate) {
        // LocalDate를 LocalDateTime으로 변환합니다 (시간은 자정으로 설정)
        LocalDateTime localDateTime = localDate.atStartOfDay();

        // LocalDateTime을 ZonedDateTime으로 변환합니다 (시간대는 시스템 기본 시간대 사용)
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());

        // ZonedDateTime을 Instant로 변환합니다
        Instant instant = zonedDateTime.toInstant();
        return instant;
    }
}
