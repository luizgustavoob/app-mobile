package br.com.paraondeirapp.sincronizacao.impl;

import android.app.ProgressDialog;
import android.content.Context;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import br.com.paraondeirapp.model.Endereco;
import br.com.paraondeirapp.constantes.IConstantesServidor;
import br.com.paraondeirapp.dao.impl.EnderecoDAO;
import br.com.paraondeirapp.sincronizacao.SincronizacaoAbstract;

public class SincronizacaoEndereco extends SincronizacaoAbstract<Endereco> {

    private Context context;

    public SincronizacaoEndereco(Context context, ProgressDialog progressDialog) {
        super(progressDialog, 4, IConstantesServidor.URL_SINCRONIZA_ENDERECOS, "");
        this.context = context;
    }

    @Override
    protected boolean isPost() {
        return false;
    }

    @Override
    protected Type getCollectionType() {
        return new TypeToken<List<Endereco>>() {}.getType();
    }

    @Override
    protected void salvarSincronizacao(List<Endereco> lista) {
        try {
            EnderecoDAO enderecoDAO = new EnderecoDAO(context);
            for (int i = 0; i < lista.size(); i++){
                Endereco endereco = lista.get(i);
                enderecoDAO.save(endereco);
            }
            enderecoDAO.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
