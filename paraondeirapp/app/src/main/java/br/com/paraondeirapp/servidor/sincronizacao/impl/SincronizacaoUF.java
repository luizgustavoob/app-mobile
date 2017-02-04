package br.com.paraondeirapp.servidor.sincronizacao.impl;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import br.com.paraondeirapp.entity.Estado;
import br.com.paraondeirapp.interfaces.IConstantesServidorSinc;
import br.com.paraondeirapp.persistence.dao.EstadoDAO;
import br.com.paraondeirapp.servidor.sincronizacao.Sincronizacao;

public class SincronizacaoUF extends Sincronizacao<Estado> {

    private Context ctx;

    public SincronizacaoUF(Context ctx, ProgressDialog progressDialog) {
        super(progressDialog, 2, IConstantesServidorSinc.LINK_SINCRONIZACAO_ESTADOS, "");
        this.ctx = ctx;
    }

    @Override
    public boolean isPost() {
        return false;
    }

    @Override
    protected Type getCollectionType() {
        return new TypeToken<Collection<Estado>>() {}.getType();
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
