package br.com.paraondeirapp.servidor.sincronizacao;

import android.app.ProgressDialog;
import android.util.Log;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;
import br.com.paraondeirapp.utils.ConexaoUtils;

public abstract class Sincronizacao<T> {

    private ProgressDialog progressDialog;
    private int etapa;
    private String linkSincronizacao;
    private String jsonPost;

    public Sincronizacao(ProgressDialog progressDialog, int etapa, String linkSincronizacao, String jsonPost) {
        this.progressDialog = progressDialog;
        this.etapa = etapa;
        this.linkSincronizacao = linkSincronizacao;
        this.jsonPost = jsonPost;
    }

    // Implementar nos descendentes.
    protected abstract boolean isPost();
    protected abstract Type getCollectionType();
    protected abstract void salvarSincronizacao(List<T> lista);

    private void atualizarProgressBar() {
        if (progressDialog != null) { // Serviço background manda null.
            progressDialog.setMax(6);
            progressDialog.setProgress(etapa);
        }
    }

    private List<T> post() throws Exception {
        List<T> lista = null;
        try {
            InputStream stream = ConexaoUtils.post(linkSincronizacao, jsonPost);
            if (stream != null){
                Reader reader = new InputStreamReader(stream);
                lista = (List<T>) new Gson().fromJson(reader, getCollectionType());
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
        return lista;
    }

    private List<T> get() throws Exception {
        List<T> lista = null;
        try {
            InputStream stream = ConexaoUtils.get(linkSincronizacao);
            if (stream != null) {
                Reader reader = new InputStreamReader(stream);
                lista = (List<T>) new Gson().fromJson(reader, getCollectionType());
            }
        } catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
        return lista;
    }

    public void sincronizar() throws Exception {
        Log.i("Sincronizacao.sincronizar()", Calendar.getInstance() + " >> Sincronizando etapa " + etapa);
        atualizarProgressBar();
        try {
            if (isPost()) {
                salvarSincronizacao(post());
            } else {
                salvarSincronizacao(get());
            }
        } catch (Exception ex){
            throw new Exception("Erro na etapa " + etapa + ". " + ex.getMessage());
        }
    }
}
