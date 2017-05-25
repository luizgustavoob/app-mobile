package br.com.paraondeirapp.sincronizacao.impl;

import android.app.ProgressDialog;
import android.content.Context;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import br.com.paraondeirapp.model.Avaliacao;
import br.com.paraondeirapp.constantes.IConstantesServidor;
import br.com.paraondeirapp.dao.impl.AvaliacaoDAO;
import br.com.paraondeirapp.sincronizacao.SincronizacaoAbstract;

public class SincronizacaoAvaliacao extends SincronizacaoAbstract<Avaliacao> {

    private Context context;

    public SincronizacaoAvaliacao(Context context, ProgressDialog progressDialog, String body) {
        super(progressDialog, 6, IConstantesServidor.URL_SINCRONIZA_AVALIACOES, body);
        this.context = context;
    }

    @Override
    protected boolean isPost() {
        return true;
    }

    @Override
    protected Type getCollectionType() {
        return new TypeToken<List<Avaliacao>>() {}.getType();
    }

    @Override
    protected void salvarSincronizacao(List<Avaliacao> lista) {
        try {
            AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO(context);
            for (int i = 0; i < lista.size(); i++){
                avaliacaoDAO.delete(lista.get(i));
            }
            avaliacaoDAO.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
