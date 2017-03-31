package br.com.paraondeirapp.model;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String usuario;
    private String fcmid;

    public Usuario(){
        super();
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFcmid() {
        return fcmid;
    }

    public void setFcmid(String fcmid){
        this.fcmid = fcmid;
    }

    @Override
    public String toString() {
        return this.usuario;
    }

}
