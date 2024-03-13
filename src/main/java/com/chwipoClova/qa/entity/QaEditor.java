package com.chwipoClova.qa.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class QaEditor {

    private String answer;

    private Integer delFlag;

    private Date delDate;

    @Builder
    public QaEditor(String answer, Integer delFlag, Date delDate) {
        this.answer = answer;
        this.delFlag = delFlag;
        this.delDate = delDate;
    }
}
