package info.ryandorman.simplescheduler.common;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Assists with application localization. Helps obtain the correct times and languages for the current locale.
 */
public class L10nUtil {
    /**
     * Prevents direct class instantiation. Methods should be accessed statically.
     */
    private L10nUtil() {
    }

    /**
     * Gets the language string associated with a given key from <code>languageBundle_**.properties</code> based on the
     * default locale detected.
     *
     * @param bundleKey Associated with a value in <code>languageBundle_**.properties</code>
     * @return Language string for the locale
     */
    public static String getLanguage(String bundleKey) {
        ResourceBundle rb = ResourceBundle.getBundle("languageBundle", Locale.getDefault());
        return rb.getString(bundleKey);
    }

    /**
     * Converts UTC time coming from a <code>java.sql.Timestamp</code> to a <code>java.time.ZonedDateTime</code> based
     * on the current ZoneId.
     *
     * @param timestamp UTC time to be converted
     * @return Local time based on the current timezone
     */
    public static ZonedDateTime utcToLocal(Timestamp timestamp) {
        return ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Converts local time stored in a <code>java.time.ZonedDateTime</code> to UTC in the form of a
     * <code>java.sql.Timestamp</code>.
     *
     * @param zonedDateTime Local time to be converted
     * @return UTC time
     */
    public static Timestamp LocalToUtc(ZonedDateTime zonedDateTime) {
        return Timestamp.from(Instant.from(ZonedDateTime.ofInstant(zonedDateTime.toInstant(), ZoneOffset.UTC)));
    }
}
