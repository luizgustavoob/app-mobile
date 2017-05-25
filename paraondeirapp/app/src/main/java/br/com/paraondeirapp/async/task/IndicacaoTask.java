package br.com.paraondeirapp.async.task;

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

import br.com.paraondeirapp.R;
import br.com.paraondeirapp.delegate.DelegateTask;
import br.com.paraondeirapp.model.Estabelecimento;
import br.com.paraondeirapp.model.Usuario;
import br.com.paraondeirapp.constantes.IConstantesServidor;
import br.com.paraondeirapp.utils.ConexaoUtils;

public class IndicacaoTask extends AsyncTask<Usuario, Void, List<Estabelecimento>> {

    private Context context;
    private DelegateTask delegate;
    private String erro;
    private ProgressDialog progressDialog;

    public IndicacaoTask(Context context, DelegateTask delegate) {
        this.context = context;
        this.delegate = delegate;
        this.erro = "";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.msg_indicacao));
        progressDialog.setTitle(context.getString(R.string.app_name));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected List<Estabelecimento> doInBackground(Usuario... params) {
        List<Estabelecimento> estabelecimentos = null;
        try {
            Gson gson = new Gson();
            JsonElement element = gson.toJsonTree(params[0], new TypeToken<Usuario>() {}.getType());
            String usuario = element.getAsJsonObject().toString();
            InputStream inputStream = ConexaoUtils.post(IConstantesServidor.URL_SOLICITA_INDICACAO, usuario);
            if (inputStream != null) {
                Reader reader = new InputStreamReader(inputStream);
                estabelecimentos = new Gson().fromJson(reader, new TypeToken<List<Estabelecimento>>() {}.getType());
            }
        } catch (Exception ex) {
            estabelecimentos = null;
            erro = ex.getMessage();
            cancel(true);
        }
        return estabelecimentos;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        progressDialog.dismiss();
        delegate.executarQuandoErro(erro);
    }

    @Override
    protected void onPostExecute(List<Estabelecimento> estabelecimentos) {
        super.onPostExecute(estabelecimentos);
        progressDialog.dismiss();
        delegate.executarQuandoSucessoNaIndicacao(estabelecimentos);
    }
}