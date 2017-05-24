package br.com.paraondeirapp.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import br.com.paraondeirapp.R;
import br.com.paraondeirapp.delegate.IDelegateTask;
import br.com.paraondeirapp.model.Avaliacao;
import br.com.paraondeirapp.sincronizacao.ListaSincronizacao;

public class SincronizacaoTask extends AsyncTask<String, String, String> {

    private Context ctx;
    private IDelegateTask delegate;
    private List<Avaliacao> listaAvaliacao;
    private String erro;
    private ProgressDialog progressDialog;

    public SincronizacaoTask(Context ctx, IDelegateTask delegate, List<Avaliacao> listaAvaliacao) {
        this.ctx = ctx;
        this.delegate = delegate;
        this.listaAvaliacao = listaAvaliacao;
        this.erro = "";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
            new ListaSincronizacao(progressDialog).sincronizarTudo(ctx, listaAvaliacao);
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
        delegate.executarQuandoErro(erro);
    }

    @Override
    protected void onPostExecute(String retorno) {
        progressDialog.dismiss();
        delegate.executarQuandoSucesso();
    }
}