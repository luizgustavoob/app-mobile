package br.com.paraondeirapp.sincronizacao.impl;

import android.app.ProgressDialog;
import android.content.Context;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import br.com.paraondeirapp.model.Estabelecimento;
import br.com.paraondeirapp.constantes.IConstantesServidor;
import br.com.paraondeirapp.dao.impl.EstabelecimentoDAO;
import br.com.paraondeirapp.sincronizacao.SincronizacaoAbstract;

public class SincronizacaoEstabelecimento extends SincronizacaoAbstract<Estabelecimento> {

    private Context context;

    public SincronizacaoEstabelecimento(Context context, ProgressDialog progressDialog) {
        super(progressDialog, 5, IConstantesServidor.URL_SINCRONIZA_ESTABELECIMENTOS, "");
        this.context = context;
    }

    @Override
    protected boolean isPost() {
        return false;
    }

    @Override
    protected Type getCollectionType() {
        return new TypeToken<List<Estabelecimento>>() {}.getType();
    }

    @Override
    protected void salvarSincronizacao(List<Estabelecimento> lista) {
        try {
            EstabelecimentoDAO estabelecimentoDAO = new EstabelecimentoDAO(context);
            for (int i = 0; i < lista.size(); i++){
                Estabelecimento estabelecimento = lista.get(i);
                estabelecimentoDAO.save(estabelecimento);
            }
            estabelecimentoDAO.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
