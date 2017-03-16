package br.com.paraondeirapp.servidor.sincronizacao.impl;

import android.app.ProgressDialog;
import android.content.Context;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import br.com.paraondeirapp.model.Endereco;
import br.com.paraondeirapp.constantes.IConstantesServidor;
import br.com.paraondeirapp.repository.dao.EnderecoDAO;
import br.com.paraondeirapp.servidor.sincronizacao.Sincronizacao;

public class SincronizacaoEndereco extends Sincronizacao<Endereco> {

    private Context ctx;

    public SincronizacaoEndereco(Context ctx, ProgressDialog progressDialog) {
        super(progressDialog, 4, IConstantesServidor.LINK_SINCRONIZACAO_ENDERECOS, "");
        this.ctx = ctx;
    }

    @Override
    protected boolean isPost() {
        return false;
    }

    @Override
    protected Type getCollectionType() {
        return new TypeToken<Collection<Endereco>>() {}.getType();
    }

    @Override
    protected void salvarSincronizacao(List<Endereco> lista) {
        try {
            EnderecoDAO enderecoDAO = new EnderecoDAO(ctx);
            for (int i = 0; i < lista.size(); i++){
                Endereco end = lista.get(i);
                enderecoDAO.save(end);
            }
            enderecoDAO.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
