package es.jarroyo.cleanproject.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by javierarroyo on 6/10/17.
 */

public class DateUtils {
    /**
     * A partir del timestamp en millisegundos obtiene la fecha en formato May 5, 2017 con hora dependiendo del locale
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

    /**
     * A partir del timestamp en millisegundos obtiene la fecha en formato May 5, 2017 dependiendo del locale
     * @param time
     * @return
     */
    public static String getDateOnlyMonthString(long time){

        try {
            Timestamp timeStamp = new Timestamp(time);
            Date date = new Date(timeStamp.getTime());

            DateTime dateTime = new DateTime(date);
            DateTimeFormatter fmt = DateTimeFormat.forStyle("L-").withLocale(Locale.getDefault());
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

    public static boolean isCurrentDateInOtherDayThanBefore(String strNextTimeStamp, String strCurrentTimestamp){
        int frequency = 0;
        Calendar calCurrent = new GregorianCalendar();
        Calendar calPrevious = new GregorianCalendar();

        try {
            //Comparamos fecha solo por dia (las horas no importan) queremos saber si son dias distintos.
            SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
            Timestamp nextTimeStamp = new Timestamp(Long.valueOf(strNextTimeStamp) * 1000);
            Date nextDate = new Date(nextTimeStamp.getTime());
            nextDate = new Date(sf.format(nextDate));

            Timestamp currentTimeStamp = new Timestamp(Long.valueOf(strCurrentTimestamp) * 1000);
            Date currentDate = new Date(currentTimeStamp.getTime());
            currentDate = new Date(sf.format(currentDate));

            calCurrent.setTime(nextDate);
            calPrevious.setTime(currentDate);

            Date d1,d2;
            d1=calCurrent.getTime();
            d2=calPrevious.getTime();
            frequency =(int)(TimeUnit.DAYS.convert(d2.getTime() - d1.getTime(), TimeUnit.MILLISECONDS));

            if(frequency == 0){
                return false;
            } else {
                return true;
            }

        }catch (Exception e){
            return false;
        }


    }
}
