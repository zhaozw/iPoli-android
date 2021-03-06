package io.ipoli.android.app.utils;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static Calendar getTodayAtMidnight() {
        return getTodayAtMidnightWithTimeZone(TimeZone.getDefault());
    }

    private static Calendar getTodayAtMidnightWithTimeZone(TimeZone timeZone) {
        Calendar c = Calendar.getInstance(timeZone);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        return c;
    }

    public static Date now() {
        return Calendar.getInstance().getTime();
    }

    public static boolean isSameDay(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return false;
        }

        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isToday(Date date) {
        return isSameDay(date, new Date());
    }

    public static boolean isTomorrow(Date date) {
        return isSameDay(date, getTomorrow());
    }

    public static Date getTomorrow() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

    public static Date getDate(Date dueDate) {
        if (dueDate == null) {
            return null;
        }
        return toStartOfDayUTC(new LocalDate(dueDate));
    }

    public static String toDateStringUSFormat(Date date) {
        return toDateStringUSFormat(date, TimeZone.getDefault());
    }

    public static String toDateStringUSFormat(Date date, TimeZone timeZone) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        formatter.setTimeZone(timeZone);
        return formatter.format(date);
    }

    public static List<String> getNext7Days() {
        List<String> dates = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        dates.add(toDateStringUSFormat(c.getTime()));
        for (int i = 1; i < 7; i++) {
            c.add(Calendar.DAY_OF_YEAR, 1);
            dates.add(toDateStringUSFormat(c.getTime()));
        }

        return dates;
    }

    public static Date nowUTC() {
        return new Date(System.currentTimeMillis());
    }

    public static Date toStartOfDayUTC(LocalDate localDate) {
        return localDate.toDateTimeAtStartOfDay(DateTimeZone.UTC).toDate();
    }

    public static Date toStartOfDay(LocalDate localDate) {
        return localDate.toDateTimeAtStartOfDay().toDate();
    }

    public static boolean isTodayUTC(LocalDate localDate) {
        return localDate.isEqual(toStartOfDayUTCLocalDate(new LocalDate()));
    }

    public static boolean isTomorrowUTC(LocalDate localDate) {
        return localDate.isEqual(toStartOfDayUTCLocalDate(new LocalDate().plusDays(1)));
    }

    private static LocalDate toStartOfDayUTCLocalDate(LocalDate localDate) {
        return localDate.toDateTimeAtStartOfDay(DateTimeZone.UTC).toLocalDate();
    }

    public static boolean isTodayUTC(Date date) {
        return isTodayUTC(new LocalDate(date, DateTimeZone.UTC));
    }

    public static boolean isTomorrowUTC(Date date) {
        return isTomorrowUTC(new LocalDate(date, DateTimeZone.UTC));
    }

    public static Date UTCToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return new DateTime(date, DateTimeZone.UTC).toLocalDate().toDate();
    }

    @NonNull
    public static List<Pair<LocalDate, LocalDate>> getBoundsForWeeksInThePast(LocalDate currentDate, int weeks) {
        LocalDate weekStart = currentDate.minusWeeks(weeks - 1).dayOfWeek().withMinimumValue();
        LocalDate weekEnd = weekStart.dayOfWeek().withMaximumValue();

        List<Pair<LocalDate, LocalDate>> weekBounds = new ArrayList<>();
        weekBounds.add(new Pair<>(weekStart, weekEnd));
        for (int i = 0; i < weeks - 1; i++) {
            weekStart = weekStart.plusWeeks(1);
            weekEnd = weekStart.dayOfWeek().withMaximumValue();
            weekBounds.add(new Pair<>(weekStart, weekEnd));
        }
        return weekBounds;
    }
}