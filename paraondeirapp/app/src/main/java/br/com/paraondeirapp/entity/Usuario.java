package br.com.paraondeirapp.entity;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String email;

    public Usuario(String email){
        super();
        this.setEmail(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return this.email;
    }

}
