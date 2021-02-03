package info.ryandorman.simplescheduler.common;

import java.time.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A utility class that provides static methods to help with application localization. Helps obtain the correct times
 * and languages to be used before the data is displayed in the view.
 */
public class L10nUtil {
    public static final Locale locale = Locale.getDefault();
    public static final ZoneId zoneId = ZoneId.systemDefault();

    public static String getLanguage(String bundleKey) {
        ResourceBundle rb = ResourceBundle.getBundle("languageBundle", locale);
        return rb.getString(bundleKey);
    }
}
