package com.example.qlycv.form.staff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddStaffForm {
    private int id;
    private int id_group;
    private String name;
    private String note;
}
