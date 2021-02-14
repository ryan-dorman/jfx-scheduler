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
 * A utility class that provides static methods to help with application localization. Helps obtain the correct times
 * and languages to be used before the data is displayed in the view.
 */
public class L10nUtil {
    /**
     * Get the language string associated with a given key from <code>languageBundle_**.properties</code> based on the
     * default locale detected.
     *
     * @param bundleKey Associated with a value in <code>languageBundle_**.properties</code>
     * @return Language string for the locale
     */
    public static String getLanguage(String bundleKey) {
        ResourceBundle rb = ResourceBundle.getBundle("languageBundle", Locale.getDefault());
        return rb.getString(bundleKey);
    }

    public static ZonedDateTime utcToLocal(Timestamp timestamp) {
            return ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault());
    }

    public static Timestamp LocalToUtc(ZonedDateTime zonedDateTime) {
        return Timestamp.from(Instant.from(ZonedDateTime.ofInstant(zonedDateTime.toInstant(), ZoneOffset.UTC)));
    }
}
