package br.com.paraondeirapp.sincronizacao;

import android.app.ProgressDialog;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import br.com.paraondeirapp.utils.ConexaoUtils;

public abstract class SincronizacaoAbstract<T> {

    private ProgressDialog progressDialog;
    private int etapa;
    private String urlSincronizacao;
    private String body;

    protected abstract boolean isPost();
    protected abstract Type getCollectionType();
    protected abstract void salvarSincronizacao(List<T> lista);

    public SincronizacaoAbstract(ProgressDialog progressDialog, int etapa, String urlSincronizacao, String body) {
        this.progressDialog = progressDialog;
        this.etapa = etapa;
        this.urlSincronizacao = urlSincronizacao;
        this.body = body;
    }

    protected int getEtapa(){
        return this.etapa;
    }

    protected String getUrlSincronizacao(){
        return this.urlSincronizacao;
    }

    protected String getBody(){
        return this.body;
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
            InputStream inputStream = ConexaoUtils.post(urlSincronizacao, body);
            if (inputStream != null){
                Reader reader = new InputStreamReader(inputStream);
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
            InputStream inputStream = ConexaoUtils.get(urlSincronizacao);
            if (inputStream != null) {
                Reader reader = new InputStreamReader(inputStream);
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
                salvarSincronizacao( post() );
            } else {
                salvarSincronizacao( get() );
            }
        } catch (Exception ex){
            throw new Exception("Erro na etapa " + etapa + ". " + ex.getMessage());
        }
    }
}
