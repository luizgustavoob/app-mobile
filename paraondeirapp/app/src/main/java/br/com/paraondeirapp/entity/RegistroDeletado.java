package br.com.paraondeirapp.entity;

import java.io.Serializable;
import java.util.Calendar;

public class RegistroDeletado implements Serializable {

    private String nome_tabela;
    private int chave_tabela;

    public RegistroDeletado() {
        super();
    }

    public String getNome_tabela() {
        return nome_tabela;
    }

    public void setNome_tabela(String nome_tabela) {
        this.nome_tabela = nome_tabela;
    }

    public int getChave_tabela() {
        return chave_tabela;
    }

    public void setChave_tabela(int chave_tabela) {
        this.chave_tabela = chave_tabela;
    }
}
