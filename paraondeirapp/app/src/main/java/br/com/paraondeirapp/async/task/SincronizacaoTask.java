package br.com.paraondeirapp.async.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import br.com.paraondeirapp.R;
import br.com.paraondeirapp.delegate.DelegateTask;
import br.com.paraondeirapp.model.Avaliacao;
import br.com.paraondeirapp.observer.Observador;
import br.com.paraondeirapp.sincronizacao.ListaSincronizacao;

public class SincronizacaoTask extends AsyncTask<List<Avaliacao>, Void, Void> {

    private Context context;
    private DelegateTask delegate;
    private String erro;
    private ProgressDialog progressDialog;

    public SincronizacaoTask(Context context, DelegateTask delegate) {
        this.context = context;
        this.delegate = delegate;
        this.erro = "";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.msg_sincronizacao));
        progressDialog.setTitle(context.getString(R.string.titulo_sincronizacao));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(List<Avaliacao>... avaliacoes) {
        try {
            new ListaSincronizacao(progressDialog).sincronizarTudo(context, avaliacoes[0]);
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
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        delegate.executarQuandoSucessoNaSincronizacao();
    }
}