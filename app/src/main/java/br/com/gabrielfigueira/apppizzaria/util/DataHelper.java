package br.com.gabrielfigueira.apppizzaria.util;

import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Fabricio on 04/06/2017.
 */

public class DataHelper {
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static Timestamp strToTimestamp(String data_hora) throws ParseException {
        if (data_hora == null || data_hora.isEmpty())
            return null;
        Date parsedDate = (Date) df.parse(data_hora);
        return new Timestamp(parsedDate.getTime());
    }

    public static Date strToDate(String data) throws ParseException {
        if (data == null || data.isEmpty())
            return null;
        return (Date) df.parse(data);
    }

    public static String dateToStr(Date data){
        if (data == null)
            return null;
        return df.format(data);
    }
}
