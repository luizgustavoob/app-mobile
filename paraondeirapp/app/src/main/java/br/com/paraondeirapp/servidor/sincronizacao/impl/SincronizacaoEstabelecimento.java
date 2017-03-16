package br.com.paraondeirapp.servidor.sincronizacao.impl;

import android.app.ProgressDialog;
import android.content.Context;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import br.com.paraondeirapp.entity.Estabelecimento;
import br.com.paraondeirapp.interfaces.IConstantesServidor;
import br.com.paraondeirapp.persistence.dao.EstabelecimentoDAO;
import br.com.paraondeirapp.servidor.sincronizacao.Sincronizacao;

public class SincronizacaoEstabelecimento extends Sincronizacao<Estabelecimento> {

    private Context ctx;

    public SincronizacaoEstabelecimento(Context ctx, ProgressDialog progressDialog) {
        super(progressDialog, 5, IConstantesServidor.LINK_SINCRONIZACAO_ESTABELECIMENTOS, "");
        this.ctx = ctx;
    }

    @Override
    protected boolean isPost() {
        return false;
    }

    @Override
    protected Type getCollectionType() {
        return new TypeToken<Collection<Estabelecimento>>() {}.getType();
    }

    @Override
    protected void salvarSincronizacao(List<Estabelecimento> lista) {
        try {
            EstabelecimentoDAO estabDao = new EstabelecimentoDAO(ctx);
            for (int i = 0; i < lista.size(); i++){
                Estabelecimento estab = lista.get(i);
                estabDao.save(estab);
            }
            estabDao.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
