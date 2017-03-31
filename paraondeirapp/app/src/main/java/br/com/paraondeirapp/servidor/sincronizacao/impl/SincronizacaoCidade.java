package br.com.paraondeirapp.servidor.sincronizacao.impl;

import android.app.ProgressDialog;
import android.content.Context;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import br.com.paraondeirapp.model.Cidade;
import br.com.paraondeirapp.constantes.IConstantesServidor;
import br.com.paraondeirapp.repository.dao.CidadeDAO;
import br.com.paraondeirapp.servidor.sincronizacao.Sincronizacao;

public class SincronizacaoCidade extends Sincronizacao<Cidade> {

    private Context ctx;

    public SincronizacaoCidade(Context ctx, ProgressDialog progressDialog) {
        super(progressDialog, 3, IConstantesServidor.LINK_SINCRONIZACAO_CIDADES, "");
        this.ctx = ctx;
    }

    @Override
    protected boolean isPost() {
        return false;
    }

    @Override
    protected Type getCollectionType() {
        return new TypeToken<List<Cidade>>() {}.getType();
    }

    @Override
    protected void salvarSincronizacao(List<Cidade> lista) {
        try {
            CidadeDAO cidadeDAO = new CidadeDAO(ctx);
            for (int i = 0; i < lista.size(); i++){
                Cidade cidade = lista.get(i);
                cidadeDAO.save(cidade);
            }
            cidadeDAO.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
