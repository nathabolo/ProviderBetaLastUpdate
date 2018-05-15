package com.tradesman.provider.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Elluminati Mohit on 1/13/2017.
 */

public class Validation {


    public static boolean eMailValidation(String emailstring) {
        if (null == emailstring || emailstring.length() == 0) {
            return false;
        }
        Pattern emailPattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher emailMatcher = emailPattern.matcher(emailstring);
        return emailMatcher.matches();
    }

    public static boolean numberValidate(String numberString){

        if (null == numberString || numberString.length() == 0) {
            return false;
        }
        Pattern emailPattern = Pattern
                .compile("^\\d{0,5}(\\.\\d{0,5})?$");
        Matcher emailMatcher = emailPattern.matcher(numberString);
        return emailMatcher.matches();
    }
}
