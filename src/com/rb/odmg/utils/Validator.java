package com.rb.odmg.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static final String PHONE_PATTERN = ""
            + "^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?([2-4]{1}[0-9]{2}[ ]?)\\d{5}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$"
            + "|^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";

    public static final int MIN_LENGTH = 2;

    public static boolean isPhoneNumber(String value){
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(value.trim().replaceAll("\\s", ""));

        return matcher.find();
    }

    public static boolean isInteger(String value){
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(value.trim().replaceAll("\\s", ""));

        return matcher.find();
    }

    public static boolean isDecimal(String value){
        Pattern pattern = Pattern.compile("(\\d+)\\.(\\d+)");
        Matcher matcher = pattern.matcher(value.trim().replaceAll("\\s", ""));

        return matcher.find() || isInteger(value);
    }

    public static boolean isLength(String value, int expectedLength){
        return value.trim().replaceAll("\\s", "").length() == expectedLength;
    }

    public static boolean isMinLength(String value){
        return value.trim().replaceAll("\\s", "").length() >= MIN_LENGTH;
    }

    public static boolean isEmpty(String value){
        return value.trim().replaceAll("\\s", "").length() == 0;
    }

    public static String normalize(String value){
        String sanitizedLib = value.trim().replaceAll("\\s{2,}", " ");
        String normalizedLib = sanitizedLib.substring(0,1).toUpperCase() + sanitizedLib.substring(1).toLowerCase();

        return normalizedLib;
    }

    public static double truncateDouble(double d){
        long aux = (long) (d*100);
        double truncated = aux/100d;

        return truncated;
    }

    public static float truncateFloat(float f){
        int aux = (int) (f*100);
        float truncated = aux/100f;

        return truncated;
    }
}
