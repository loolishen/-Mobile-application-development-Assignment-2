package com.fit2081.assignment_2;

import java.util.Random;



public class Utils {

    private static char randomChar() {
        Random r = new Random();
        char c = (char) (r.nextInt(26) + 'a');
        return c;
    }

    private static String randomInt(int len) {
        Random rand = new Random();

        int n = rand.nextInt((int) Math.pow(10, len));
        int generateLen = String.valueOf(n).length();
        if (generateLen != len) {
            return "0" + n;
        }
        return n + "";
    }

    public static String generateEventId() {
        String id;
        id = ("E" + randomChar() + randomChar()).toUpperCase() + "-" + randomInt(5);
        return id;
    }

    public static String generateCategoryId() {
        String id;
        id = ("C" + randomChar() + randomChar()).toUpperCase() + "-" + randomInt(4);
        return id;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static boolean isAlphaNumeric(String s){
        String pattern= "^[a-zA-Z0-9 ]*$";
        return s.matches(pattern);
    }

    public static final String TAG_KEY = "A2FIT201";
}
