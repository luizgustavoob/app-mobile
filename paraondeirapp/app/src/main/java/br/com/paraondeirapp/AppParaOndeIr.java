package br.com.paraondeirapp;

import android.app.Application;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;
import br.com.paraondeirapp.entity.Estabelecimento;
import br.com.paraondeirapp.entity.Usuario;
import br.com.paraondeirapp.enumeration.TipoConsultaEstabelecimento;
import br.com.paraondeirapp.interfaces.IObservador;
import br.com.paraondeirapp.observer.NotificacaoObservadora;
import br.com.paraondeirapp.service.SincronizacaoService;

public class AppParaOndeIr extends Application {

    //Para manter em memória algumas informações úteis.
    private static AppParaOndeIr uniqueInstance = null;
    private Usuario user;
    private List<Estabelecimento> estabelecimentos;
    private List<IObservador> observadoresSincronizacao;
    private TipoConsultaEstabelecimento tipoConsulta;

    public static AppParaOndeIr getInstance(){
        if (uniqueInstance == null)
            throw new IllegalStateException("Configure a aplicação no AndroidManifest.xml");

        return uniqueInstance;
    }

    @Override
    public void onCreate() {
        //File file = new File("/data/data/br.com.projetoparaondeir/databases/paraondeir_db");
        //file.delete();
        estabelecimentos = new ArrayList<>();
        observadoresSincronizacao = new ArrayList<>();
        observadoresSincronizacao.add(new NotificacaoObservadora());
        uniqueInstance = this;
        Intent i = new Intent(this, SincronizacaoService.class);
        startService(i);
    }

    @Override
    public void onTerminate() {
        this.user = null;
        this.estabelecimentos = null;
        this.observadoresSincronizacao = null;
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

    public List<IObservador> getObservadoresSincronizacao() {
        return this.observadoresSincronizacao;
    }
}
