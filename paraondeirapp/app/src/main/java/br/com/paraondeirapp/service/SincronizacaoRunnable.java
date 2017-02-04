package br.com.paraondeirapp.service;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.paraondeirapp.entity.Avaliacao;
import br.com.paraondeirapp.utils.ConexaoUtils;
import br.com.paraondeirapp.persistence.dao.AvaliacaoDAO;
import br.com.paraondeirapp.interfaces.IObservador;
import br.com.paraondeirapp.servidor.sincronizacao.ListaSincronizacao;

public class SincronizacaoRunnable implements Runnable {

    private Context ctx;
    private List<IObservador> observadores;

    public SincronizacaoRunnable(Context ctx) {
        this.ctx = ctx;
        this.observadores = new ArrayList<>();
    }

    public SincronizacaoRunnable addObservador(IObservador observador){
        this.observadores.add(observador);
        return this;
    }

    @Override
    public void run() {
        Log.i("SincronizacaoRunnable", "Sincronizacao automática iniciada em " + Calendar.getInstance());
        try {
            if (ConexaoUtils.temConexao(ctx)) {
                AvaliacaoDAO dao = new AvaliacaoDAO(ctx);
                List<Avaliacao> avaliacoes = dao.findAll();
                dao.close();

                ListaSincronizacao sincronizacao = new ListaSincronizacao(null);
                sincronizacao.sincronizarTudo(ctx, avaliacoes);

                //NotificacaoObservadora
                if (observadores != null && observadores.size() > 0) {
                    for (IObservador observador : observadores) {
                        observador.notificarObservadores();
                    }
                }
                Log.i("SincronizacaoRunnable", "Sincronizacao automática finalizada em " + Calendar.getInstance());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
