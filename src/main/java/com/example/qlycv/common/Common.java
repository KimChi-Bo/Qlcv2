package com.example.qlycv.common;

import java.util.Calendar;
import java.util.Date;

public class Common {

	public static Date addTime(Date date, int field, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(field, amount);
		date = c.getTime();
		return date;
	}
}
