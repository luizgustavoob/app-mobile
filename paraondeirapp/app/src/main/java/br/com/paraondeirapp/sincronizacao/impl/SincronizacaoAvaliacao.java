package br.com.paraondeirapp.sincronizacao.impl;

import android.app.ProgressDialog;
import android.content.Context;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import br.com.paraondeirapp.model.Avaliacao;
import br.com.paraondeirapp.constantes.IConstantesServidor;
import br.com.paraondeirapp.dao.impl.AvaliacaoDAO;
import br.com.paraondeirapp.sincronizacao.Sincronizacao;

public class SincronizacaoAvaliacao extends Sincronizacao<Avaliacao> {

    private Context ctx;

    public SincronizacaoAvaliacao(Context ctx, ProgressDialog progressDialog, String jsonPost) {
        super(progressDialog, 6, IConstantesServidor.LINK_SINCRONIZACAO_AVALIACOES, jsonPost);
        this.ctx = ctx;
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
            AvaliacaoDAO dao = new AvaliacaoDAO(ctx);
            for (int i = 0; i < lista.size(); i++){
                dao.delete(lista.get(i));
            }
            dao.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
