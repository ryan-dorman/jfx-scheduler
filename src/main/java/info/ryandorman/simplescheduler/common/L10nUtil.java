package info.ryandorman.simplescheduler.common;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A utility class that provides static methods to help with application localization. Helps obtain the correct times
 * and languages to be used before the data is displayed in the view.
 */
public class L10nUtil {
    public static final Locale locale = Locale.getDefault();
    public static final ZoneId zoneId = ZoneId.systemDefault();

    /**
     * Get the language string associated with a given key from <code>languageBundle_**.properties</code> based on the
     * default locale detected.
     *
     * @param bundleKey Associated with a value in <code>languageBundle_**.properties</code>
     * @return Language string for the locale
     */
    public static String getLanguage(String bundleKey) {
        ResourceBundle rb = ResourceBundle.getBundle("languageBundle", locale);
        return rb.getString(bundleKey);
    }

    public static ZonedDateTime utcToLocal(Timestamp timestamp) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()), zoneId);
    }
}
