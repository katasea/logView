package cn.katasea.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author katasea
 * @date 2019/5/22 14:20
 */
public class DateUtil {
	private static final long ND = 86400000L;
	public static final String DATEFORMATLONG17 = "yyyyMMdd HH:mm:ss";
	public static final String DATEFORMATLONG = "yyyy-MM-dd HH:mm:ss";
	public static final String DATEFORMATMEDIUM = "yyyy-MM-dd HH:mm";
	public static final String DATE_FORMAT_YMDH = "yyyyMMddHH";
	public static final String DATEFORMATSHORT = "yyyy-MM-dd";
	public static final String DATE_SHORT_YEARMONTH = "yyyy-MM";
	public static final String DATENUMBERFORMAT = "yyyyMMdd";
	public static final String DATEHOURNUMBERFORMAT = "yyyyMMddHH";
	public static final String DATE_FORMAT_HOUR = "H";
	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

	public DateUtil() {
	}

	public static Date getCurrentDate() {
		return new Date();
	}

	public static long getCurrentTimeMillis() {
		return System.currentTimeMillis();
	}

	public static String getCurrentYearMonthDate() {
		return getCurrentFormatDate("yyyy-MM");
	}

	public static int cutTwoDateToDay(Date a, Date b) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		int theday = 0;

		try {
			Date beginDate = format.parse(format.format(a));
			Date endDate = format.parse(format.format(b));
			calendar.setTime(beginDate);
			long begin = calendar.getTimeInMillis();
			calendar.setTime(endDate);
			long end = calendar.getTimeInMillis();
			theday = (int)((end - begin) / 86400000L);
		} catch (ParseException var11) {
			logger.debug("日期转换出错!", var11);
		}

		return theday;
	}

	public static String intToTimeString(int time) {
		String hour = String.valueOf(time / 60);
		String minute = String.valueOf(time - time / 60 * 60);
		if (hour.length() < 2) {
			hour = "0" + hour;
		}

		if (minute.length() < 2) {
			minute = "0" + minute;
		}

		return hour + ":" + minute;
	}

	public static Date maxDate(Date data1, Date data2) {
		return data1.before(data2) ? data2 : data1;
	}

	public static Date minDate(Date a, Date b) {
		return a.before(b) ? a : b;
	}

	public static int getWeekOfDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(7) - 1;
		if (w == 0) {
			w = 7;
		}

		return w;
	}

	public static int getDayOfWeek() {
		int dayOfWeek = Calendar.getInstance().get(7);
		if (dayOfWeek == 1) {
			dayOfWeek = 7;
		} else {
			--dayOfWeek;
		}

		return dayOfWeek;
	}

	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	public static Date createDate(String dateString, String pattern) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.parse(dateString);
	}

	public static String getCurrentFormatDate(String formatDate) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
		Date dateInstance = getCurrentDate();
		simpleDateFormat.applyPattern(formatDate);
		return simpleDateFormat.format(dateInstance);
	}

	public static String getCurrentFormatDateLong() {
		return getCurrentFormatDate("yyyy-MM-dd HH:mm:ss");
	}

	public static String getCurrentFormatDateLong17() {
		return getCurrentFormatDate("yyyyMMdd HH:mm:ss");
	}

	public static String getCurrentFormatDateMedium() {
		return getCurrentFormatDate("yyyy-MM-dd HH:mm");
	}

	public static String getCurrentFormatDateShort() {
		return getCurrentFormatDate("yyyy-MM-dd");
	}

	private static String getDate2String(String format, Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
		simpleDateFormat.applyPattern(format);
		return simpleDateFormat.format(date);
	}

	public static String getDate2LongString(Date date) {
		return getDate2String("yyyy-MM-dd HH:mm:ss", date);
	}

	public static String getDate2LongString17(Date date) {
		return getDate2String("yyyyMMdd HH:mm:ss", date);
	}

	public static String getDate2MediumString(Date date) {
		return getDate2String("yyyy-MM-dd HH:mm", date);
	}

	public static String getDate2ShortString(Date date) {
		return getDate2String("yyyy-MM-dd", date);
	}

	public static String getDate2HourString(Date date) {
		return getDate2String("H", date);
	}

	public static String getDate2NumberString(Date date) {
		return getDate2String("yyyyMMdd", date);
	}

	public static String getLong2LongString(long l) {
		Date dateInstance = getLong2Date(l);
		return getDate2LongString(dateInstance);
	}

	public static String getLong2MediumString(long l) {
		Date dateInstance = getLong2Date(l);
		return getDate2MediumString(dateInstance);
	}

	public static String getLong2ShortString(long l) {
		Date dateInstance = getLong2Date(l);
		return getDate2ShortString(dateInstance);
	}

	private static Date getString2Date(String format, String str) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
		simpleDateFormat.applyPattern(format);
		ParsePosition parseposition = new ParsePosition(0);
		return simpleDateFormat.parse(str, parseposition);
	}

	public static Date getString2LongDate(String str) {
		return getString2Date("yyyy-MM-dd HH:mm:ss", str);
	}

	public static Date getString2LongDate17(String str) {
		return getString2Date("yyyyMMdd HH:mm:ss", str);
	}

	public static Date getString2MediumDate(String str) {
		return getString2Date("yyyy-MM-dd HH:mm", str);
	}

	public static Date getString2YmdhDate(String str) {
		return getString2Date("yyyyMMddHH", str);
	}

	public static Date getString2ShortDate(String str) {
		return getString2Date("yyyy-MM-dd", str);
	}

	public static Date getEmptyDate() {
		return getString2ShortDate("1971-01-01");
	}

	public static String getEmptyDateString() {
		return "1971-01-01";
	}

	public static Date getLong2Date(long l) {
		return new Date(l);
	}

	public static int getOffMinutes(long l) {
		return getOffMinutes(l, getCurrentTimeMillis());
	}

	public static int getOffMinutes(long from, long to) {
		return (int)((to - from) / 60000L);
	}

	public static int getYear() {
		return Calendar.getInstance().get(1);
	}

	public static int getMonth() {
		return Calendar.getInstance().get(2) + 1;
	}

	public static int getDay() {
		return Calendar.getInstance().get(5);
	}

	public static int getHour() {
		return Calendar.getInstance().get(11);
	}

	public static int getMinute() {
		return Calendar.getInstance().get(12);
	}

	public static int getSecond() {
		return Calendar.getInstance().get(13);
	}

	public static String add(int day) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.add(5, day);
		Date dateInstance = gregorianCalendar.getTime();
		return getDate2String("yyyy-MM-dd", dateInstance);
	}

	public static String subtract(int day) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.add(5, -day);
		Date dateInstance = gregorianCalendar.getTime();
		return getDate2String("yyyy-MM-dd", dateInstance);
	}

	public static String getlastMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		GregorianCalendar gc = new GregorianCalendar();
		gc.roll(2, false);
		return sdf.format(gc.getTime());
	}

	public static String getCurrentLastMonth() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Calendar cal = Calendar.getInstance();
		cal.add(2, -1);
		return format.format(cal.getTime());
	}

	public static String[] getCurrentLastWeek() {
		String[] weeks = new String[2];
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(6, -cal.get(7));
		weeks[1] = format.format(cal.getTime());
		cal.add(6, -6);
		weeks[0] = format.format(cal.getTime());
		return weeks;
	}

	public static boolean isStartLessEndTime(String start, String end) throws ParseException {
		if (!start.equals("") && !end.equals("")) {
			Date startDate = DateFormat.getDateInstance().parse(start);
			Date endDate = DateFormat.getDateInstance().parse(end);
			return startDate.compareTo(endDate) < 0;
		} else {
			return false;
		}
	}

	public static Date getToday() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

	public static Date getYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(5, -1);
		return cal.getTime();
	}

	public static Date getFirstDayOfThisWeek() {
		Date today = getToday();
		return getFirstDayInWeek(today);
	}

	public static Date getFirstDayInMonth(int year, int month) {
		--month;
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month, 1);
		return cal.getTime();
	}

	public static Date getLastDayInMonth(int year, int month) {
		Date firstDay = getFirstDayInMonth(year, month);
		Calendar firstCal = getCalendarByDate(firstDay);
		firstCal.add(2, 1);
		firstCal.add(6, -1);
		return firstCal.getTime();
	}

	public static Date getFirstDayOfThisMonth() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(1);
		int month = cal.get(2);
		++month;
		return getFirstDayInMonth(year, month);
	}

	public static Date getFirstDayOfLastMonth() {
		int year = getYearOfLastMonth();
		int month = getMonthOfLastMonth();
		return getFirstDayInMonth(year, month);
	}

	private static int getYearOfLastMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(2, -1);
		return cal.get(1);
	}

	private static int getMonthOfLastMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(2, -1);
		int month = cal.get(2);
		++month;
		return month;
	}

	public static int getThisMonth() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(2);
		++month;
		return month;
	}

	public static Date getFirstDayInYear(int year) {
		int month = 0;
		int day = 1;
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		return cal.getTime();
	}

	public static Date getLastDayInYear(int year) {
		int month = 11;
		int day = 31;
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		return cal.getTime();
	}

	public static int getThisYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(1);
	}

	public static int getLastYear() {
		Calendar cal = Calendar.getInstance();
		cal.add(1, -1);
		return cal.get(1);
	}

	public static Date getFirstDayInWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int thisweek = cal.get(3);
		int lastweek = thisweek;

		Date thisday;
		for(thisday = null; lastweek == thisweek; lastweek = cal.get(3)) {
			thisday = cal.getTime();
			cal.add(5, -1);
		}

		return thisday;
	}

	public static Date getLastDayInWeek(Date date) {
		Date firstdayofweek = getFirstDayInWeek(date);
		Calendar cal = getCalendarByDate(firstdayofweek);
		cal.add(6, 6);
		return cal.getTime();
	}

	private static Calendar getCalendarByDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static String getDateStr(Date date) {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	public static String getMonthStr(Date date) {
		String pattern = "yyyy-MM";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	public static Date getDateByStr(String dateStr) {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = null;

		try {
			date = format.parse(dateStr);
			return date;
		} catch (ParseException var5) {
			throw new RuntimeException("wrong date format, should be " + pattern);
		}
	}

	public static String addHour(int hour) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.add(10, hour);
		Date dateInstance = gregorianCalendar.getTime();
		return getDate2String("yyyyMMddHH", dateInstance);
	}

	public static Date addHour2Date(int hour) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.add(10, hour);
		Date dateInstance = gregorianCalendar.getTime();
		return dateInstance;
	}

	public static String addHourReturnShortDate(int hour) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.add(10, hour);
		Date dateInstance = gregorianCalendar.getTime();
		return getDate2String("yyyy-MM-dd", dateInstance);
	}

	public static Date getNextHourDate(int hour) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.add(10, hour);
		Date dateInstance = gregorianCalendar.getTime();
		String longDateTime = getDate2LongString(dateInstance);
		String NextDateHour = longDateTime.subSequence(0, 14) + "00:00";
		return getString2LongDate(NextDateHour);
	}

	public static Date getNextMinuteDate(int minute) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.add(12, minute);
		Date dateInstance = gregorianCalendar.getTime();
		String longDateTime = getDate2LongString(dateInstance);
		String NextDateMinute = longDateTime.subSequence(0, 17) + "00";
		return getString2LongDate(NextDateMinute);
	}

	public static boolean inDateRange(Date startDate, Date endDate, Date date) {
		if (startDate != null && endDate != null && date != null) {
			return (startDate.before(date) || startDate.equals(date)) && (endDate.after(date) || endDate.equals(date));
		} else {
			return false;
		}
	}

	public static String getTime() {
		return format(new Date(), "HHmmss");
	}

	public static String getDate() {
		return format(new Date(), "yyyyMMdd");
	}

	public static String format(Date date, String format) {
		String result = "";
		if (date != null) {
			DateFormat df = new SimpleDateFormat(format);
			result = df.format(date);
		}

		return result;
	}

	public static String getStrCurrent() {
		Date date = new Date();
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateChar = sim.format(date);
		return dateChar;
	}

	public static String getCurrentDateTime() {
		Date date = new Date();
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dateChar = sim.format(date);
		return dateChar;
	}

	public static String date2Str(Date date, String format) {
		if (null == date) {
			return null;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		}
	}

	public static String addDay(String date, int n) {
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
		Date date1 = null;

		try {
			date1 = ft.parse(date);
		} catch (ParseException var5) {
			var5.printStackTrace();
		}

		Long longTime = date1.getTime();
		longTime = longTime + (long)(86400000 * n);
		return ft.format(new Date(longTime));
	}

	public static String formatDate(String pattern, Date date) {
		SimpleDateFormat sdf = null;
		String dateStr = "";
		if (null != pattern && !"".equals(pattern)) {
			sdf = new SimpleDateFormat(pattern);
		} else {
			sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		}

		if (null == date) {
			dateStr = sdf.format(new Date());
		} else {
			dateStr = sdf.format(date);
		}

		return dateStr;
	}

	public static String formatStrToDateStr(String str, String p1, String p2) {
		String result = null;
		if (!CommonUtil.isEmpty(str) && !CommonUtil.isEmpty(p1) && !CommonUtil.isEmpty(p2)) {
			SimpleDateFormat sim = new SimpleDateFormat(p1);
			SimpleDateFormat sim2 = new SimpleDateFormat(p2);

			try {
				Date date = sim.parse(str);
				result = sim2.format(date);
			} catch (ParseException var7) {
				var7.printStackTrace();
			}
		} else {
			result = "";
		}

		return result;
	}

	public static Calendar getDateOfLastMonth(Calendar date) {
		Calendar lastDate = (Calendar)date.clone();
		lastDate.add(2, -1);
		return lastDate;
	}

	public static String getDateOfLastMonth(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		try {
			Date date = sdf.parse(dateStr);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			Calendar cal = getDateOfLastMonth(c);
			return sdf.format(cal.getTime());
		} catch (ParseException var5) {
			return null;
		}
	}

	public static long getQuot(String time1, String time2) {
		long quot = 0L;
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");

		try {
			Date date1 = ft.parse(time1);
			Date date2 = ft.parse(time2);
			quot = date1.getTime() - date2.getTime();
			quot = quot / 1000L / 60L / 60L / 24L;
		} catch (ParseException var7) {
			var7.printStackTrace();
		}

		return quot;
	}

	public static boolean isValidDate(String date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setLenient(false);

		try {
			dateFormat.parse(date);
			return true;
		} catch (ParseException var4) {
			return false;
		}
	}

	public static String getAddDateTime(String dateTime, String format, int addNum) {
		DateFormat df = new SimpleDateFormat(format);
		Date dd = null;

		try {
			dd = df.parse(dateTime);
		} catch (ParseException var6) {
			var6.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dd);
		calendar.add(5, addNum);
		return df.format(calendar.getTime());
	}

	public static String dateWithoutSeparator(String dateString) {
		return dateString.replace("-", "").replace(":", "").replace(" ", "");
	}

	public static String time6WithSeparator(String timeString) {
		String hour = timeString.substring(0, 2);
		String min = timeString.substring(2, 4);
		String sec = timeString.substring(4, 6);
		return hour + ":" + min + ":" + sec;
	}

	public static long dateDiff(String startdate, String enddate, String format) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date startdt = sdf.parse(startdate);
		Date enddt = sdf.parse(enddate);
		long diff = enddt.getTime() - startdt.getTime();
		long day = diff / 86400000L;
		return day;
	}

	public static int getSeconds(Date startdate, Date enddate) {
		long time = enddate.getTime() - startdate.getTime();
		int totalS = (new Long(time / 1000L / 60L)).intValue();
		return totalS;
	}

	public static String getDateSeconds(String startDate, String endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsss");
		boolean var3 = false;

		int datetime;
		try {
			Date start = sdf.parse(startDate);
			Date end = sdf.parse(endDate);
			datetime = getSeconds(start, end);
		} catch (ParseException var6) {
			return null;
		} catch (Exception var7) {
			return null;
		}

		return String.valueOf(datetime);
	}

	public static String addMonth(String month, int addMonth) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		month = month.substring(0, 4) + "-" + month.substring(4, 6) + "-01";
		Date date = null;

		try {
			date = sdf.parse(month);
		} catch (ParseException var5) {
			var5.printStackTrace();
		}

		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		cd.add(2, addMonth);
		return sdf.format(cd.getTime()).substring(0, 7).replace("-", "");
	}

	public static String getLastDayOfMonth(String month) {
		int year = Integer.valueOf(month.substring(0, 4));
		int monthInt = Integer.valueOf(month.substring(4, month.length()));
		Date date = getLastDayInMonth(year, monthInt);
		return format(date, "yyyyMMdd");
	}
}
