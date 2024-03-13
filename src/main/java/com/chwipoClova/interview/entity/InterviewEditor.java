package com.chwipoClova.interview.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;


@Getter
public class InterviewEditor {

    private Integer status;

    private String feedback;

    private Integer delFlag;

    private Date delDate;

    @Builder
    public InterviewEditor(Integer status, String feedback, Integer delFlag, Date delDate) {
        this.status = status;
        this.feedback = feedback;
        this.delFlag = delFlag;
        this.delDate = delDate;
    }
}
