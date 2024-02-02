package com.example.qlycv.model.response;

import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain.Strategy.Content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
public class ReportDTO {
    private Integer id;
    private String reportContent;
    private Float reportTime;
    private Integer planId;
    private String reportDate;
    private Integer reportStatus;
    private String reportReason;
}
