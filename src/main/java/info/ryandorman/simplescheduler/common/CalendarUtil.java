package info.ryandorman.simplescheduler.common;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.EnumSet;

public class CalendarUtil {
    public static ZonedDateTime getFirstDayOfWeek() {
        ZonedDateTime today = ZonedDateTime.now(ZoneId.systemDefault());
        return today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).truncatedTo(ChronoUnit.DAYS);
    }

    public static ZonedDateTime getLastDayOfWeek() {
        ZonedDateTime today = ZonedDateTime.now(ZoneId.systemDefault());
        return today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).truncatedTo(ChronoUnit.DAYS);
    }

    public static ZonedDateTime getFirstDayOfMonth() {
        ZonedDateTime today = ZonedDateTime.now(ZoneId.systemDefault());
        return today.with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
    }

    public static ZonedDateTime getLastDayOfMonth() {
        ZonedDateTime today = ZonedDateTime.now(ZoneId.systemDefault());
        return today.with(TemporalAdjusters.lastDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
    }

    public static ZonedDateTime getFirstDayOfYear() {
        ZonedDateTime today = ZonedDateTime.now(ZoneId.systemDefault());
        return today.with(TemporalAdjusters.firstDayOfYear()).truncatedTo(ChronoUnit.DAYS);
    }

    public static ZonedDateTime getLastDayOfYear() {
        ZonedDateTime today = ZonedDateTime.now(ZoneId.systemDefault());
        return today.with(TemporalAdjusters.lastDayOfYear()).truncatedTo(ChronoUnit.DAYS);
    }

    public static boolean isWeekend(DayOfWeek dayOfWeek) {
        return EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(dayOfWeek);
    }
}
