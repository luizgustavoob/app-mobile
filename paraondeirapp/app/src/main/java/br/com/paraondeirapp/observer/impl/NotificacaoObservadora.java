package br.com.paraondeirapp.observer.impl;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import br.com.paraondeirapp.AppParaOndeIr;
import br.com.paraondeirapp.R;
import br.com.paraondeirapp.constantes.IConstantesNotificacao;
import br.com.paraondeirapp.utils.NotificacaoUtils;
import br.com.paraondeirapp.observer.Observador;
import br.com.paraondeirapp.view.ListaActivity;

public class NotificacaoObservadora implements Observador {

    @Override
    public void notificarObservadores() {
        Context context = AppParaOndeIr.getInstance();
        PendingIntent intent = PendingIntent.getActivity(context, 0, new Intent(context, ListaActivity.class), 0);
        NotificacaoUtils.notificar(IConstantesNotificacao.NOTIFICA_SINCRONIZACAO, context, intent,
                context.getString(R.string.app_name), context.getString(R.string.app_name),
                context.getString(R.string.msg_sucesso_sincronizacao));
    }
}