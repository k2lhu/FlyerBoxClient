package com.flyerbox.logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tmrafael on 24.10.2014.
 */
public class CheckData  {
    public static boolean checkName(String name) {
        Pattern p = Pattern.compile("^[a-zA-Z]+(([',\\.\\- ][a-zA-Z ])?[a-zA-Z]*)*$");
        Matcher m = p.matcher(name);
        return m.matches();
    }

    public static boolean checkEmail(String email) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9]+([_\\-\\.][a-zA-Z0-9]+)*@[a-zA-Z0-9]+([-\\.][a-zA-Z0-9]+)*\\.[a-z]{2,6}$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean checkPass(String password) {
        Pattern p = Pattern.compile("^([\\w\\. @*#',$%^=+-]{8,30})$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public static boolean checkCountryOrCity(String countryOrCity) {
        Pattern p = Pattern.compile("^[a-zA-Z]+(([\\- ][a-zA-Z ])?[a-zA-Z]*)*$");
        Matcher m = p.matcher(countryOrCity);
        return m.matches();
    }
}
