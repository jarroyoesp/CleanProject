package es.jarroyo.cleanproject.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;

/**
 * Created by javierarroyo on 6/10/17.
 */

public class DateUtils {
    /**
     * A partir del timestamp en millisegundos obtiene la fecha en formato May 5, 2017 dependiendo del locale
     * @param time
     * @return
     */
    public static String getDateWithMonthString(long time){

        try {
            Timestamp timeStamp = new Timestamp(time);
            Date date = new Date(timeStamp.getTime());

            DateTime dateTime = new DateTime(date);
            DateTimeFormatter fmt = DateTimeFormat.forStyle("LS").withLocale(Locale.getDefault());
            String str = dateTime.toString(fmt);

            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getHoursAndMinutesFrom(long timeInMill) {
        try {
            Timestamp timeStamp = new Timestamp(timeInMill);
            Date date = new Date(timeStamp.getTime());

            DateTime dateTime = new DateTime(date);
            DateTimeFormatter fmt = DateTimeFormat.forStyle("-S").withLocale(Locale.getDefault());
            String str = dateTime.toString(fmt);

            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
