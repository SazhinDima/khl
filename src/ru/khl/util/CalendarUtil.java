package ru.khl.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarUtil {

	private static CalendarUtil instance;

	public static synchronized CalendarUtil getInstance() {
		if (instance == null) {
			instance = new CalendarUtil();
		}
		return instance;
	}

	private final List<Month> months = new ArrayList<Month>();

	String[] monthName = { "Январь", "Февраль", "Март", "Апрель", "Май",
			"Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь",
			"Декабрь" };

	public List<Month> getMonths(Date startDate, Date endDate) {
		List<Month> months = new ArrayList<Month>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		months.add(new Month(calendar.get(Calendar.MONTH), calendar
				.get(Calendar.YEAR), monthName[calendar.get(Calendar.MONTH)]));
		while (calendar.getTime().before(endDate)) {
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DATE,
					calendar.getActualMaximum(Calendar.DATE));
			months.add(new Month(calendar.get(Calendar.MONTH), calendar
					.get(Calendar.YEAR),
					monthName[calendar.get(Calendar.MONTH)]));
		}

		return months;
	}

	public class Month {

		private int month;
		private int year;
		private String monthName;

		public Month(int month, int year, String monthName) {
			this.month = month;
			this.year = year;
			this.monthName = monthName;
		}

		public int getMonth() {
			return month;
		}

		public void setMonth(int month) {
			this.month = month;
		}

		public int getYear() {
			return year;
		}

		public void setYear(int year) {
			this.year = year;
		}

		public String getMonthName() {
			return monthName;
		}

		public void setMonthName(String monthName) {
			this.monthName = monthName;
		}

	}

}
