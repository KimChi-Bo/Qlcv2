package com.example.qlycv.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Plan {
	private String planWeek;
	private String planYear;
	private int limit;
	private int offset;
	private String startDate;
	private String endDate;
	private	Integer group;

}
