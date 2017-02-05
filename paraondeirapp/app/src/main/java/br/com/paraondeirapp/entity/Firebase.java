package br.com.paraondeirapp.entity;

import java.io.Serializable;

public class Firebase implements Serializable {

    private String token;

    public String getToken(){
        return this.token;
    }

    public void setToken(String token){
        this.token = token;
    }
}
