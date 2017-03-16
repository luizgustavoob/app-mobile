package br.com.paraondeirapp.servidor.sincronizacao;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import br.com.paraondeirapp.R;
import br.com.paraondeirapp.model.Avaliacao;
import br.com.paraondeirapp.utils.ConexaoUtils;
import br.com.paraondeirapp.delegate.IDelegateSinc;

public class RealizaSincronizacao {

    private Context ctx;
    private ProgressDialog progressDialog;
    private IDelegateSinc delegate;
    private List<Avaliacao> listaAvaliacao;

    public RealizaSincronizacao(Context ctx, IDelegateSinc delegate,
                                List<Avaliacao> listaAvaliacao) {
        this.ctx = ctx;
        this.delegate = delegate;
        this.listaAvaliacao = listaAvaliacao;
        if (ConexaoUtils.temConexao(ctx)) {
            new SincronizacaoTask().execute();
        }
    }

    private class SincronizacaoTask extends AsyncTask<String, String, String> {

        private String erro;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            erro = "";
            progressDialog = new ProgressDialog(ctx);
            progressDialog.setMessage(ctx.getString(R.string.msg_sincronizacao));
            progressDialog.setTitle(ctx.getString(R.string.titulo_sincronizacao));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.i("SincronizacaoTask.doInBackground", Calendar.getInstance() + " >> Sincronização manual iniciada.");
                ListaSincronizacao sincronizacao = new ListaSincronizacao(progressDialog);
                sincronizacao.sincronizarTudo(ctx, listaAvaliacao);
            } catch (Exception ex) {
                erro = ex.getMessage();
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressDialog.dismiss();
            delegate.processarErroSinc(erro);
        }

        @Override
        protected void onPostExecute(String retorno) {
            Log.i("SincronizacaoTask.onPostExecute()", Calendar.getInstance() + " >> Sincronização manual finalizada.");
            progressDialog.dismiss();
            delegate.processarRetornoSinc();
        }
    }
}
