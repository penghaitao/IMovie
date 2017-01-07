package com.wartechwick.imovie.tools;

/**
 * Created by penghaitao on 2016/10/14.
 */
public class StringUtils {

    public static String getYear(String releaseDate) {
        String[] dates = releaseDate.split("-");
        return dates[0];
    }
}
