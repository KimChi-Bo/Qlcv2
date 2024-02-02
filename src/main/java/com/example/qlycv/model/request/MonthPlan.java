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
public class MonthPlan {
    private int Id;
    private int idField;
    private int month;
    private int year;
    private String content;
}
