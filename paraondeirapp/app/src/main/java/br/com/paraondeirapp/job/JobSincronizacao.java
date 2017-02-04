package br.com.paraondeirapp.job;

import android.util.Log;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Calendar;
import java.util.List;

import br.com.paraondeirapp.AppParaOndeIr;
import br.com.paraondeirapp.entity.Avaliacao;
import br.com.paraondeirapp.interfaces.IObservador;
import br.com.paraondeirapp.persistence.dao.AvaliacaoDAO;
import br.com.paraondeirapp.servidor.sincronizacao.ListaSincronizacao;
import br.com.paraondeirapp.utils.ConexaoUtils;

public class JobSincronizacao implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            if (ConexaoUtils.temConexao(AppParaOndeIr.getInstance())) {
                Log.i("JobSincronizacao.execute()", Calendar.getInstance() + " >> Job de sincronização iniciado");
                AvaliacaoDAO dao = new AvaliacaoDAO(AppParaOndeIr.getInstance());
                List<Avaliacao> avaliacoes = dao.findAll();
                dao.close();

                ListaSincronizacao sincronizacao = new ListaSincronizacao(null);
                sincronizacao.sincronizarTudo(AppParaOndeIr.getInstance(), avaliacoes);

                List<IObservador> observadores = AppParaOndeIr.getInstance().getObservadoresSincronizacao();
                if (observadores != null && observadores.size() > 0) {
                    for (IObservador observador : observadores) {
                        observador.notificarObservadores();
                    }
                }
                Log.i("JobSincronizacao.execute()", Calendar.getInstance() + " >> Job de sincronização finalizado");
            }
        } catch (Exception ex) {
            Log.i("JobSincronizacao.execute()", Calendar.getInstance() + " >> Erro: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
