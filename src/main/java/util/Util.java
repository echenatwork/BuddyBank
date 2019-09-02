package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    private static final SimpleDateFormat DATE_PICKER_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    public static Date parseFromDatePicker(String dateStr) throws ParseException {
        return DATE_PICKER_FORMAT.parse(dateStr);
    }

}
