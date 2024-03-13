package com.chwipoClova.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommonCode {
    DELETE_Y(1, "삭제"),
    DELETE_N(0, "미삭제")
    ;

    private final Integer code;
    private final String name;
}
