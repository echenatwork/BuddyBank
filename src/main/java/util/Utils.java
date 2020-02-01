package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class Utils {
    private static final SimpleDateFormat DATE_PICKER_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    public static Date parseFromDatePicker(String dateStr) throws ParseException {
        return DATE_PICKER_FORMAT.parse(dateStr);
    }

    public static Instant parseLocalTimeZoneInstant(String string) {
        return LocalDateTime.parse(string).atZone(ZoneId.systemDefault()).toInstant();
    }

    public static <E> Collection<E> emptyIfNull(Collection<E> collection) {
        if (collection == null) {
            return Collections.EMPTY_LIST;
        }
        return collection;
    }

}
