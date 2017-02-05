package br.com.paraondeirapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import br.com.paraondeirapp.AppParaOndeIr;
import br.com.paraondeirapp.interfaces.IConstantesPreferences;

public class SharedPreferencesUtils {

    public SharedPreferences getSharedPreferences(){
        return AppParaOndeIr.getInstance()
                .getSharedPreferences(IConstantesPreferences.APP_NAME, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor(){
        return getSharedPreferences().edit();
    }

    public boolean isPrimeiroAcesso(){
        return getSharedPreferences().getBoolean(IConstantesPreferences.PREF_PRIMEIRO_ACESSO, true);
    }

    public void setPrimeiroAcesso(boolean primeiroAcesso){
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(IConstantesPreferences.PREF_PRIMEIRO_ACESSO, primeiroAcesso);
        editor.commit();
    }

    public String getIPServidor(){
        return getSharedPreferences().getString(IConstantesPreferences.PREF_IP_SERVIDOR, "");
    }

    public void setIPServidor(String ipServidor){
        SharedPreferences.Editor editor = getEditor();
        editor.putString(IConstantesPreferences.PREF_IP_SERVIDOR, ipServidor);
        editor.commit();
    }

    public void setTokenFirebase(String token){
        SharedPreferences.Editor editor = getEditor();
        editor.putString(IConstantesPreferences.PREF_TOKEN_FIREBASE, token);
        editor.commit();
    }

    public String getTokenFirebase(){
        return getSharedPreferences().getString(IConstantesPreferences.PREF_TOKEN_FIREBASE, "");
    }
}
