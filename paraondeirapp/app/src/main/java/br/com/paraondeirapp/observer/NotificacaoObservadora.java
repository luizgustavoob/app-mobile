package br.com.paraondeirapp.observer;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import br.com.paraondeirapp.AppParaOndeIr;
import br.com.paraondeirapp.R;
import br.com.paraondeirapp.interfaces.IConstantesNotificacao;
import br.com.paraondeirapp.utils.NotificacaoUtils;
import br.com.paraondeirapp.interfaces.IObservador;
import br.com.paraondeirapp.view.ListaActivity;

public class NotificacaoObservadora implements IObservador {

    @Override
    public void notificarObservadores() {
        Context ctx = AppParaOndeIr.getInstance();
        PendingIntent intent = PendingIntent.getActivity(ctx, 0, new Intent(ctx, ListaActivity.class), 0);
        NotificacaoUtils.notificar(IConstantesNotificacao.NOTIFICA_SINCRONIZACAO, ctx, intent,
                ctx.getString(R.string.app_name), ctx.getString(R.string.app_name),
                ctx.getString(R.string.fim_sincronizacao));
    }
}