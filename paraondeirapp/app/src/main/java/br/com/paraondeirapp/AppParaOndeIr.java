package br.com.paraondeirapp;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import br.com.paraondeirapp.enumeration.UltimaVisualizacao;
import br.com.paraondeirapp.model.Estabelecimento;
import br.com.paraondeirapp.model.Usuario;

public class AppParaOndeIr extends Application {

    //Para manter em memória algumas informações úteis.
    private static AppParaOndeIr uniqueInstance = null;
    private Usuario user;
    private List<Estabelecimento> estabsEmCache, ultimosEstabsIndicacao;
    private UltimaVisualizacao ultimaVisualizacao;

    public synchronized static AppParaOndeIr getInstance(){
        return uniqueInstance;
    }

    @Override
    public void onCreate() {
        ultimosEstabsIndicacao = new ArrayList<>();
        estabsEmCache = new ArrayList<>();
        uniqueInstance = this;
        ultimaVisualizacao = UltimaVisualizacao.BD_LOCAL;
    }

    @Override
    public void onTerminate() {
        this.user = null;
        this.ultimosEstabsIndicacao = null;
        this.estabsEmCache = null;
        super.onTerminate();
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public List<Estabelecimento> getUltimosEstabsIndicacao() {
        return ultimosEstabsIndicacao;
    }

    public void setUltimosEstabsIndicacao(List<Estabelecimento> ultimosEstabsIndicacao) {
        this.ultimosEstabsIndicacao = ultimosEstabsIndicacao;
    }

    public List<Estabelecimento> getEstabsEmCache() {
        return estabsEmCache;
    }

    public void setEstabsEmCache(List<Estabelecimento> estabsEmCache) {
        this.estabsEmCache = estabsEmCache;
    }

    public UltimaVisualizacao getUltimaVisualizacao(){
        return ultimaVisualizacao;
    }

    public void setUltimaVisualizacao(UltimaVisualizacao ultimaVisualizacao){
        this.ultimaVisualizacao = ultimaVisualizacao;
    }
}
