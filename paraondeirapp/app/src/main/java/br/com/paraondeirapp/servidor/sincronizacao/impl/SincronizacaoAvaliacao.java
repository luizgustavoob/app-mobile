package br.com.paraondeirapp.servidor.sincronizacao.impl;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import br.com.paraondeirapp.entity.Avaliacao;
import br.com.paraondeirapp.interfaces.IConstantesServidorSinc;
import br.com.paraondeirapp.servidor.sincronizacao.Sincronizacao;

public class SincronizacaoAvaliacao extends Sincronizacao<Avaliacao> {

    private Context ctx;

    public SincronizacaoAvaliacao(Context ctx, ProgressDialog progressDialog, String jsonPost) {
        super(progressDialog, 6, IConstantesServidorSinc.LINK_SINCRONIZACAO_AVALIACOES, jsonPost);
        this.ctx = ctx;
    }

    @Override
    protected boolean isPost() {
        return true;
    }

    @Override
    protected Type getCollectionType() {
        return new TypeToken<Collection<Avaliacao>>() {}.getType();
    }

    @Override
    protected void salvarSincronizacao(List<Avaliacao> lista) {

    }
}
