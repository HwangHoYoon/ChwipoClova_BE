package com.chwipoClova.recruit.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RecruitEditor {
    private Integer delFlag;

    @Builder
    public RecruitEditor(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
