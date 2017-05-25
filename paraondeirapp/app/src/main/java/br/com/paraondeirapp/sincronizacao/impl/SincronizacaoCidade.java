package br.com.paraondeirapp.sincronizacao.impl;

import android.app.ProgressDialog;
import android.content.Context;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import br.com.paraondeirapp.model.Cidade;
import br.com.paraondeirapp.constantes.IConstantesServidor;
import br.com.paraondeirapp.dao.impl.CidadeDAO;
import br.com.paraondeirapp.sincronizacao.SincronizacaoAbstract;

public class SincronizacaoCidade extends SincronizacaoAbstract<Cidade> {

    private Context context;

    public SincronizacaoCidade(Context context, ProgressDialog progressDialog) {
        super(progressDialog, 3, IConstantesServidor.URL_SINCRONIZA_CIDADES, "");
        this.context = context;
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
            CidadeDAO cidadeDAO = new CidadeDAO(context);
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
