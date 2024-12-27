package info.ryandorman.simplescheduler.common;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.EnumSet;

/**
 * Allows for easy retrieval of and information on days relative to the week, month, and year of the current date.
 */
public class CalendarUtil {
    /**
     * Prevents direct class instantiation. Methods should be accessed statically.
     */
    private CalendarUtil() {
    }

    /**
     * Provides the first date of the current week based on the current ZoneId.
     *
     * @return Date for the Monday of the current week
     */
    public static ZonedDateTime getFirstDayOfWeek() {
        ZonedDateTime today = ZonedDateTime.now(ZoneId.systemDefault());
        return today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).truncatedTo(ChronoUnit.DAYS);
    }

    /**
     * Provides the last date of the current week based on the current ZoneId.
     *
     * @return Date for the Sunday of the current week
     */
    public static ZonedDateTime getLastDayOfWeek() {
        ZonedDateTime today = ZonedDateTime.now(ZoneId.systemDefault());
        return today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).truncatedTo(ChronoUnit.DAYS);
    }

    /**
     * Provides the first date of the current month based on the current ZoneId.
     *
     * @return Date for the first day of the current month
     */
    public static ZonedDateTime getFirstDayOfMonth() {
        ZonedDateTime today = ZonedDateTime.now(ZoneId.systemDefault());
        return today.with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
    }

    /**
     * Provides the last date of the current month based on the current ZoneId.
     *
     * @return Date for the last day of the current month
     */
    public static ZonedDateTime getLastDayOfMonth() {
        ZonedDateTime today = ZonedDateTime.now(ZoneId.systemDefault());
        return today.with(TemporalAdjusters.lastDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
    }

    /**
     * Provides the first date of the current year based on the current ZoneId.
     *
     * @return Date for the first day of the current year
     */
    public static ZonedDateTime getFirstDayOfYear() {
        ZonedDateTime today = ZonedDateTime.now(ZoneId.systemDefault());
        return today.with(TemporalAdjusters.firstDayOfYear()).truncatedTo(ChronoUnit.DAYS);
    }

    /**
     * Provides the last date of the current year based on the current ZoneId.
     *
     * @return Date for the last day of the current year
     */
    public static ZonedDateTime getLastDayOfYear() {
        ZonedDateTime today = ZonedDateTime.now(ZoneId.systemDefault());
        return today.with(TemporalAdjusters.lastDayOfYear()).truncatedTo(ChronoUnit.DAYS);
    }

    /**
     * Tests to determine if a day of the week falls on a weekend (i.e., Saturday or Sunday)
     *
     * @param dayOfWeek Day of the week to test
     * @return Boolean indicating if the given day is on a weekend
     */
    public static boolean isWeekend(DayOfWeek dayOfWeek) {
        return EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(dayOfWeek);
    }
}
