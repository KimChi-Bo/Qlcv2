package com.example.qlycv.model.response;

import java.util.Date;

public interface UserPlanWeek {
    Integer getId();

    String getKpiName();

    Date getPlanDate();

    String getPlanContent();

    Float getPlanTime();

    String getStatus();

    String getReportContent();

    Float getReportTime();

    String getFieldName();

    String getApprovedContent();

    String getReportNote();

    String getUserName();

    String getGroupName();

    String getStaffName();
}
