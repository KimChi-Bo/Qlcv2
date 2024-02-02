package com.example.qlycv.form.kpi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddForm {
    private int id;
    private int id_field;
    private String content;
    private float weight;
    private int month;
    private int year;
}
