package br.com.paraondeirapp.observer.impl;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import br.com.paraondeirapp.AppParaOndeIr;
import br.com.paraondeirapp.R;
import br.com.paraondeirapp.constantes.IConstantesNotificacao;
import br.com.paraondeirapp.utils.NotificacaoUtils;
import br.com.paraondeirapp.observer.interfaces.IObservador;
import br.com.paraondeirapp.view.StartActivity;

public class NotificacaoObservadora implements IObservador {

    @Override
    public void notificarObservadores() {
        Context ctx = AppParaOndeIr.getInstance();
        PendingIntent intent = PendingIntent.getActivity(ctx, 0, new Intent(ctx, StartActivity.class), 0);
        NotificacaoUtils.notificar(IConstantesNotificacao.NOTIFICA_SINCRONIZACAO, ctx, intent,
                ctx.getString(R.string.app_name), ctx.getString(R.string.app_name),
                ctx.getString(R.string.msg_sucesso_sincronizacao));
    }
}