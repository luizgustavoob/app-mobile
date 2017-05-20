package br.com.paraondeirapp.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import br.com.paraondeirapp.AppParaOndeIr;
import br.com.paraondeirapp.R;
import br.com.paraondeirapp.delegate.IDelegate;
import br.com.paraondeirapp.model.Estabelecimento;
import br.com.paraondeirapp.model.Usuario;
import br.com.paraondeirapp.constantes.IConstantesServidor;
import br.com.paraondeirapp.utils.ConexaoUtils;

public class IndicacaoTask extends AsyncTask<String, String, String> {

    private Context ctx;
    private IDelegate delegate;
    private String erro;
    private ProgressDialog progressDialog;
    private List<Estabelecimento> lista;

    public IndicacaoTask(Context ctx, IDelegate delegate) {
        this.ctx = ctx;
        this.delegate = delegate;
        this.erro = "";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage(ctx.getString(R.string.msg_indicacao));
        progressDialog.setTitle(ctx.getString(R.string.app_name));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Gson gson = new Gson();
            JsonElement element = gson.toJsonTree(AppParaOndeIr.getInstance().getUser(),
                    new TypeToken<Usuario>() {}.getType());
            String usuario = element.getAsJsonObject().toString();

            InputStream stream = ConexaoUtils.post(IConstantesServidor.LINK_INDICACAO, usuario);
            if (stream != null) {
                Reader reader = new InputStreamReader(stream);
                lista = new Gson().fromJson(reader, new TypeToken<List<Estabelecimento>>() {}.getType());
            }
        } catch (Exception ex){
            lista = null;
            erro = ex.getMessage();
            cancel(true);
        }
        return null;
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled();
        progressDialog.dismiss();
        delegate.executarQuandoErro(erro);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        delegate.executarQuandoSucesso(lista);
    }
}