package br.com.paraondeirapp.utils;

import java.util.regex.Pattern;

public class StringUtils {

    public static String apenasNumeros(String str) {
        return Pattern.compile("[^0-9]").matcher(str).replaceAll("");
    }
}
