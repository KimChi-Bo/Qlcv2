package com.example.qlycv.model.response;

import java.text.Format;
import java.text.SimpleDateFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlanWeekDTO {
    private Integer planId;
    private String planDate;
    private String kpiName;
    private String planContent;
    private String planStatus;
    private String reportContent;
    private String fieldName;
    private String approvedContent;
    private String reportNote;
    private Float reportTime;
    private Float planTime;
    private String userName;
    private String groupName;
    private String staffName;
}
