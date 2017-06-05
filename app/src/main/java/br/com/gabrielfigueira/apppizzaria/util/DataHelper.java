package br.com.gabrielfigueira.apppizzaria.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Fabricio on 04/06/2017.
 */

public class DataHelper {
    public static Timestamp strToTimestamp(String data_hora) throws ParseException {
        if (data_hora == null || data_hora.isEmpty())
            return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        Date parsedDate = (Date) dateFormat.parse(data_hora);
        return new Timestamp(parsedDate.getTime());
    }

    public static Date strToDate(String data) throws ParseException {
        if (data == null || data.isEmpty())
            return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        return (Date) dateFormat.parse(data);
    }
}
