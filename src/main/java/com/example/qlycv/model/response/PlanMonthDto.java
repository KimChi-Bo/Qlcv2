package com.example.qlycv.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlanMonthDto {

	String planId;

	String nameUser;

	String field;

	String content;

	String monthYear;

	boolean showBtnDelFlg;
}