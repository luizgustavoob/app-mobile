package br.com.paraondeirapp.sincronizacao.impl;

import android.app.ProgressDialog;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;
import br.com.paraondeirapp.constantes.IConstantesServidor;
import br.com.paraondeirapp.model.Usuario;
import br.com.paraondeirapp.sincronizacao.SincronizacaoAbstract;
import br.com.paraondeirapp.utils.ConexaoUtils;

public class SincronizacaoUsuario extends SincronizacaoAbstract<Usuario> {

    private static String TAG = SincronizacaoUsuario.class.getSimpleName();

    public SincronizacaoUsuario(ProgressDialog progressDialog, String json){
        super(progressDialog, 7, IConstantesServidor.URL_SINCRONIZA_USUARIO, json);
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
        //Não existe tabela de usuário no app.
    }

    @Override
    protected List<Usuario> post() throws Exception {
        try {
            ConexaoUtils.post(getUrlSincronizacao(), getBody());
        } catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
        return null;
    }

    @Override
    public void sincronizar() throws Exception {
        Log.i(TAG, Calendar.getInstance() + " >> Sincronizando etapa " + getEtapa());
        super.atualizarProgressBar();
        try {
            post();
        } catch (Exception ex){
            throw new Exception("Erro na etapa " + getEtapa() + ". " + ex.getMessage());
        }
    }
}
