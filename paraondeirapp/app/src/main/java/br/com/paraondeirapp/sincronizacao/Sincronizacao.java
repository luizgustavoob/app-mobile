package br.com.paraondeirapp.sincronizacao;

import android.app.ProgressDialog;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import br.com.paraondeirapp.utils.ConexaoUtils;

public abstract class Sincronizacao<T> {

    private ProgressDialog progressDialog;
    private int etapa;
    private String linkSincronizacao;
    private String jsonPost;

    protected abstract boolean isPost();
    protected abstract Type getCollectionType();
    protected abstract void salvarSincronizacao(List<T> lista);

    public Sincronizacao(ProgressDialog progressDialog, int etapa, String linkSincronizacao, String jsonPost) {
        this.progressDialog = progressDialog;
        this.etapa = etapa;
        this.linkSincronizacao = linkSincronizacao;
        this.jsonPost = jsonPost;
    }

    protected int getEtapa(){
        return this.etapa;
    }

    protected String getLinkSincronizacao(){
        return this.linkSincronizacao;
    }

    protected String getJsonPost(){
        return this.jsonPost;
    }

    protected void atualizarProgressBar() {
        if (progressDialog != null) { // Serviço background manda null.
            progressDialog.setMax(7);
            progressDialog.setProgress(etapa);
        }
    }

    protected List<T> post() throws Exception {
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

    protected List<T> get() throws Exception {
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
