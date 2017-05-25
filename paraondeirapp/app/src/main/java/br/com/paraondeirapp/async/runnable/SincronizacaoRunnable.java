package br.com.paraondeirapp.async.runnable;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.com.paraondeirapp.model.Avaliacao;
import br.com.paraondeirapp.sincronizacao.ListaSincronizacao;
import br.com.paraondeirapp.utils.ConexaoUtils;
import br.com.paraondeirapp.dao.impl.AvaliacaoDAO;
import br.com.paraondeirapp.observer.Observador;

public class SincronizacaoRunnable implements Runnable {

    private Context context;
    private List<Observador> observadores;

    public SincronizacaoRunnable(Context context) {
        this.context = context;
        this.observadores = new ArrayList<>();
    }

    public SincronizacaoRunnable addObservador(Observador observador){
        this.observadores.add(observador);
        return this;
    }

    @Override
    public void run() {
        if (!ConexaoUtils.temConexao(context)) {
            return;
        }

        List<Avaliacao> avaliacoes = null;
        try {
            AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO(context);
            avaliacoes = avaliacaoDAO.findAll();
            avaliacaoDAO.close();
            new ListaSincronizacao(null).sincronizarTudo(context, avaliacoes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (observadores.size() > 0) {
            for (Observador observador : observadores) {
                observador.notificarObservadores();
            }
        }
    }
}
