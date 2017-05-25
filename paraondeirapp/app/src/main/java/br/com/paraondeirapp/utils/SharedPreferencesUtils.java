package br.com.paraondeirapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import br.com.paraondeirapp.AppParaOndeIr;
import br.com.paraondeirapp.constantes.IConstantesPreferences;

public class SharedPreferencesUtils {

    private static SharedPreferences getSharedPreferences() {
        return AppParaOndeIr.getInstance()
                .getSharedPreferences(IConstantesPreferences.APP_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor() {
        return getSharedPreferences().edit();
    }

    public static boolean isPrimeiroAcesso() {
        return getSharedPreferences().getBoolean(IConstantesPreferences.PREF_PRIMEIRO_ACESSO, true);
    }

    public static void setPrimeiroAcesso(boolean isPrimeiroAcesso) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(IConstantesPreferences.PREF_PRIMEIRO_ACESSO, isPrimeiroAcesso);
        editor.commit();
    }

    public static String getIPServidor() {
        return getSharedPreferences().getString(IConstantesPreferences.PREF_IP_SERVIDOR, "");
    }

    public static void setIPServidor(String ipServidor) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(IConstantesPreferences.PREF_IP_SERVIDOR, ipServidor);
        editor.commit();
    }

    public static void setTokenFirebase(String token) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(IConstantesPreferences.PREF_TOKEN_FIREBASE, token);
        editor.commit();
    }

    public static String getTokenFirebase() {
        return getSharedPreferences().getString(IConstantesPreferences.PREF_TOKEN_FIREBASE, "");
    }
}