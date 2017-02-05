package br.com.paraondeirapp;

import android.app.Application;
import java.util.ArrayList;
import java.util.List;
import br.com.paraondeirapp.entity.Estabelecimento;
import br.com.paraondeirapp.entity.Usuario;
import br.com.paraondeirapp.enumeration.TipoConsultaEstabelecimento;

public class AppParaOndeIr extends Application {

    //Para manter em memória algumas informações úteis.
    private static AppParaOndeIr uniqueInstance = null;
    private Usuario user;
    private List<Estabelecimento> estabelecimentos;
    private TipoConsultaEstabelecimento tipoConsulta;

    public static AppParaOndeIr getInstance(){
        if (uniqueInstance == null)
            throw new IllegalStateException("Configure a aplicação no AndroidManifest.xml");

        return uniqueInstance;
    }

    @Override
    public void onCreate() {
        estabelecimentos = new ArrayList<>();
        uniqueInstance = this;
    }

    @Override
    public void onTerminate() {
        this.user = null;
        this.estabelecimentos = null;
        super.onTerminate();
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public List<Estabelecimento> getEstabelecimentos() {
        return estabelecimentos;
    }

    public void setEstabelecimentos(List<Estabelecimento> estabelecimentos) {
        this.estabelecimentos = estabelecimentos;
    }

    public TipoConsultaEstabelecimento getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(TipoConsultaEstabelecimento tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }
}
