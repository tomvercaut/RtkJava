package org.rt.rtkj.dicom;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

public class DicomUtils {

    public static final int UNDEFINED_I32 = -2 ^ 31;
    public static final int UNDEFINED_U32 = -2 ^ 31;
    public static final double UNDEFINED_DOUBLE = Double.MAX_VALUE;
    public static final float UNDEFINED_FLOAT = Float.MAX_VALUE;

    public static Optional<LocalTime> tmToLocalTime(Optional<String> s) {
        if (s.isEmpty() || s.get().isBlank()) return Optional.empty();
        return Optional.ofNullable(tmToLocalTime(s.get()));
    }

    public static LocalTime tmToLocalTime(String s) {
        int n = s.length();
        if (n == 0) return LocalTime.now();
        int hour = 0;
        int minute = 0;
        int second = 0;
        int milli = 0;
        if (n >= 2) hour = Integer.parseInt(s.substring(0, 2));
        if (n >= 4) minute = Integer.parseInt(s.substring(2, 4));
        if (n >= 6) second = Integer.parseInt(s.substring(4, 6));
        if (n > 7) {
            var ts = s.substring(7, n);
            while (ts.length() < 6) {
                ts += '0';
            }
            milli = Integer.parseInt(ts);
        }
        return LocalTime.of(hour, minute, second, milli * 1000);
    }

    public static Optional<LocalDate> getLocalDateFromString(Optional<String> s) {
        if (s.isEmpty() || s.get().isBlank()) return Optional.empty();
        return Optional.ofNullable(getLocalDate(s.get()));
    }

    public static LocalDate getLocalDate(String s) {
        int n = s.length();
        if (n == 0) return LocalDate.now();
        int year = 0;
        int month = 0;
        int day = 0;
        if (n >= 4) year = Integer.parseInt(s.substring(0, 4));
        if (n >= 6) month = Integer.parseInt(s.substring(4, 6));
        if (n >= 8) day = Integer.parseInt(s.substring(6, 8));
        return LocalDate.of(year, month, day);
    }

    public static Optional<LocalDate> dateToLocalDate(Optional<Date> s) {
        if (s.isEmpty()) return Optional.empty();
        return Optional.ofNullable(dateToLocalDate(s.get()));
    }

    public static LocalDate dateToLocalDate(Date date) {
        if (date == null) return null;
        var instant = date.toInstant();
        var zoneDt = instant.atZone(ZoneId.systemDefault());
        return zoneDt.toLocalDate();
    }

    public static Optional<LocalDateTime> getLocalDateTime(Optional<String> s) {
        if (s.isEmpty() || s.get().isBlank()) return Optional.empty();
        return Optional.ofNullable(getLocalDateTime(s.get()));
    }

    public static LocalDateTime getLocalDateTime(String s) {
        int n = s.length();
        if (n < 14) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");
        LocalDate ld = getLocalDate(s);
        LocalTime lt = tmToLocalTime(s.substring(8));
        if (ld == null || lt == null) return null;
        return LocalDateTime.of(ld.getYear(), ld.getMonth(), ld.getDayOfMonth(), lt.getHour(), lt.getMinute(), lt.getSecond(), lt.getNano());
    }

    public static DateTimeFormatter getDateFormatter() {
        return DateTimeFormatter.BASIC_ISO_DATE;
    }

    public static DateTimeFormatter getTimeFormatter() {
        return DateTimeFormatter.ofPattern("HHmmss");
    }

    /**
     * Get the current date formatted as yyyyMMdd. Prepadding of digits is applied where needed.
     *
     * @return Formatted string with the current date.
     */
    public static String getLocalDateNow() {
        var d = LocalDate.now();
        return d.format(getDateFormatter());
    }

    /**
     * Get the current local time formatted as HHmmss. Prepadding of digits is applied where needed.
     *
     * @return Formatted string with the current local time.
     */
    public static String getLocalTimeNow() {
        var t = LocalTime.now();
        return t.format(getTimeFormatter());
    }

    public static String getLocalDateTimeNow() {
        return getLocalDateNow() + getLocalTimeNow();
    }
}
