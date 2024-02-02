package com.example.qlycv.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class WeekPlan {
    private Integer planId;
    private String planDate;
    private String planContent;
    private Integer planKpi;
    private String startDate;
    private float planTime;
    private Integer kpiUserId;
    private String approvedContent;
    private Integer staffId;
}
