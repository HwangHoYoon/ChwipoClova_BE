package com.chwipoClova.resume.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResumeEditor {

    private Integer delFlag;

    @Builder
    public ResumeEditor(Integer delFlag) {
        this.delFlag = delFlag;
    }

}
