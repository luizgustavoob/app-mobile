package br.com.paraondeirapp.sincronizacao.impl;

import android.app.ProgressDialog;
import android.content.Context;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import br.com.paraondeirapp.model.Estado;
import br.com.paraondeirapp.constantes.IConstantesServidor;
import br.com.paraondeirapp.dao.impl.EstadoDAO;
import br.com.paraondeirapp.sincronizacao.Sincronizacao;

public class SincronizacaoUF extends Sincronizacao<Estado> {

    private Context ctx;

    public SincronizacaoUF(Context ctx, ProgressDialog progressDialog) {
        super(progressDialog, 2, IConstantesServidor.LINK_SINCRONIZACAO_ESTADOS, "");
        this.ctx = ctx;
    }

    @Override
    public boolean isPost() {
        return false;
    }

    @Override
    protected Type getCollectionType() {
        return new TypeToken<List<Estado>>() {}.getType();
    }

    @Override
    protected void salvarSincronizacao(List<Estado> lista) {
        try {
            EstadoDAO estadoDAO = new EstadoDAO(ctx);
            for (int i = 0; i < lista.size(); i++){
                Estado uf = lista.get(i);
                estadoDAO.save(uf);
            }
            estadoDAO.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
