package br.com.paraondeirapp.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {

    public static String removerAcentos(String str){
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        return str;
    }

    public static String apenasNumeros(String str) {
        return Pattern.compile("[^0-9]").matcher(str).replaceAll("");
    }
}
