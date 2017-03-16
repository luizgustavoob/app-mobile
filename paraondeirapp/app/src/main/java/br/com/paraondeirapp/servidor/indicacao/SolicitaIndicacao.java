package br.com.paraondeirapp.servidor.indicacao;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONStringer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.List;

import br.com.paraondeirapp.R;
import br.com.paraondeirapp.entity.Estabelecimento;
import br.com.paraondeirapp.entity.Usuario;
import br.com.paraondeirapp.interfaces.IConstantesServidor;
import br.com.paraondeirapp.interfaces.IDelegateIndicacao;
import br.com.paraondeirapp.utils.ConexaoUtils;

public class SolicitaIndicacao {

    private Context ctx;
    private IDelegateIndicacao delegate;
    private ProgressDialog progressDialog;
    private Usuario user;

    public SolicitaIndicacao(Context ctx, IDelegateIndicacao delegate, Usuario usuario){
        this.ctx = ctx;
        this.delegate = delegate;
        this.user = usuario;
        new IndicacaoTask().execute();
    }

    private class IndicacaoTask extends AsyncTask<String, String, String> {

        private String erro;
        private List<Estabelecimento> lista;

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
                JSONStringer builder = new JSONStringer();
                builder.object();
                builder.key("usuario").value(user.getEmail());
                builder.endObject();
                InputStream stream = ConexaoUtils.post(IConstantesServidor.LINK_SINCRONIZACAO_INDICACAO, builder.toString());
                if (stream != null) {
                    Reader reader = new InputStreamReader(stream);
                    lista = (List<Estabelecimento>) new Gson().fromJson(reader, new TypeToken<Collection<Estabelecimento>>() {
                    }.getType());
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
            delegate.processarErroIndicacao(erro);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            delegate.processarRetornoIndicacao(lista);
        }
    }
}
