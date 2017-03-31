package br.com.paraondeirapp.servidor.sincronizacao.impl;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;
import br.com.paraondeirapp.constantes.IConstantesServidor;
import br.com.paraondeirapp.model.Usuario;
import br.com.paraondeirapp.servidor.sincronizacao.Sincronizacao;
import br.com.paraondeirapp.utils.ConexaoUtils;

public class SincronizacaoUsuario extends Sincronizacao<Usuario> {

    private Context ctx;

    public SincronizacaoUsuario(Context ctx, ProgressDialog progressDialog, String json){
        super(progressDialog, 7, IConstantesServidor.LINK_SINCRONIZACAO_FIREBASE, json);
        this.ctx = ctx;
    }

    @Override
    protected boolean isPost() {
        return true;
    }

    @Override
    protected Type getCollectionType() {
        return new TypeToken<Usuario>() {}.getType();
    }

    @Override
    protected void salvarSincronizacao(List<Usuario> lista) {

    }

    @Override
    protected List<Usuario> post() throws Exception {
        try {
            ConexaoUtils.post(getLinkSincronizacao(), getJsonPost());
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
