package com.neevtech.droolsweb.model;

import java.util.Calendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;

public class CurrentDateTime {
	private static final Logger logger = Logger.getLogger(CurrentDateTime.class);
	private int year;
	private int dayOfYear;
	
	public CurrentDateTime() {
		super();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		this.year = calendar.get(Calendar.YEAR);
		this.dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		logger.info("CurrentDateTime: "+ calendar.getTime());
	}
	
	public int getDayOfYear() {
		return dayOfYear;
	}
	public void setDayOfYear(int dayOfYear) {
		this.dayOfYear = dayOfYear;
	}

	public boolean isDiscountApplicable(int fromDate, int fromMonth, int toDate, int toMonth){
		return calculateDayOfYear(fromDate, fromMonth) <= dayOfYear && dayOfYear <= calculateDayOfYear(toDate, toMonth);
	}
	
	private int calculateDayOfYear(int date, int month){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
		calendar.set(year, month-1, date);
		return calendar.get(Calendar.DAY_OF_YEAR);
	}
}
