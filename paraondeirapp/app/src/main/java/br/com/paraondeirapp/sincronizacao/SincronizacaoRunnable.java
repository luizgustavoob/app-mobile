package br.com.paraondeirapp.sincronizacao;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.com.paraondeirapp.model.Avaliacao;
import br.com.paraondeirapp.utils.ConexaoUtils;
import br.com.paraondeirapp.dao.impl.AvaliacaoDAO;
import br.com.paraondeirapp.observer.IObservador;

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
        try {
            if (ConexaoUtils.temConexao(ctx)) {
                AvaliacaoDAO dao = new AvaliacaoDAO(ctx);
                List<Avaliacao> avaliacoes = dao.findAll();
                dao.close();

                ListaSincronizacao sincronizacao = new ListaSincronizacao(null);
                sincronizacao.sincronizarTudo(ctx, avaliacoes);

                if (observadores != null && observadores.size() > 0) {
                    for (IObservador observador : observadores) {
                        observador.notificarObservadores();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
