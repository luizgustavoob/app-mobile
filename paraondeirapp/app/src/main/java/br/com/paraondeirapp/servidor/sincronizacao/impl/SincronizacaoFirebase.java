package br.com.paraondeirapp.servidor.sincronizacao.impl;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;

import br.com.paraondeirapp.entity.Firebase;
import br.com.paraondeirapp.interfaces.IConstantesServidor;
import br.com.paraondeirapp.servidor.sincronizacao.Sincronizacao;
import br.com.paraondeirapp.utils.ConexaoUtils;

public class SincronizacaoFirebase extends Sincronizacao<Firebase> {

    public SincronizacaoFirebase(Context ctx, ProgressDialog progressDialog, String json){
        super(progressDialog, 7, IConstantesServidor.LINK_SINCRONIZACAO_FIREBASE, json);
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

    @Override
    protected List<Firebase> post() throws Exception {
        try {
            InputStream stream = ConexaoUtils.post(getLinkSincronizacao(), getJsonPost());
        } catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
        return null;
    }

    @Override
    public void sincronizar() throws Exception {
        Log.i("Sincronizacao.sincronizar()", Calendar.getInstance() + " >> Sincronizando etapa " + getEtapa());
        atualizarProgressBar();
        try {
            post();
        } catch (Exception ex){
            throw new Exception("Erro na etapa " + getEtapa() + ". " + ex.getMessage());
        }
    }
}
