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
public class DayReport {
    private Integer Id;
    private String planDate;
    private String planContent;
    private float planTime;
    private Integer planId;

}
