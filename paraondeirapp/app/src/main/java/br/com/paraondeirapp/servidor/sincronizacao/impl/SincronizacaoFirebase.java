package br.com.paraondeirapp.servidor.sincronizacao.impl;

import android.app.ProgressDialog;
import android.content.Context;

import java.lang.reflect.Type;
import java.util.List;

import br.com.paraondeirapp.entity.Firebase;
import br.com.paraondeirapp.interfaces.IConstantesServidorSinc;
import br.com.paraondeirapp.servidor.sincronizacao.Sincronizacao;

public class SincronizacaoFirebase extends Sincronizacao<Firebase> {

    public SincronizacaoFirebase(Context ctx, ProgressDialog progressDialog, String json){
        super(progressDialog, 7, IConstantesServidorSinc.LINK_SINCRONIZACAO_FIREBASE, json);
    }

    @Override
    protected boolean isPost() {
        return true;
    }

    @Override
    protected Type getCollectionType() {
        return null;
    }

    @Override
    protected void salvarSincronizacao(List<Firebase> lista) {

    }
}
