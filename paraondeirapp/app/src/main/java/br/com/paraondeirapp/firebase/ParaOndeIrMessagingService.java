package br.com.paraondeirapp.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import br.com.paraondeirapp.async.task.SincronizacaoTask;
import br.com.paraondeirapp.dao.impl.AvaliacaoDAO;
import br.com.paraondeirapp.model.Avaliacao;
import br.com.paraondeirapp.observer.impl.NotificacaoObservadora;
import br.com.paraondeirapp.async.runnable.SincronizacaoRunnable;
import br.com.paraondeirapp.utils.ConexaoUtils;

public class ParaOndeIrMessagingService extends FirebaseMessagingService {

    private static String TAG = ParaOndeIrMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, "Recebeu a mensagem de notificação");
        Map<String, String> map = remoteMessage.getData();
        if (map.containsKey("sincronizar")) {
            if (map.get("sincronizar").trim().equalsIgnoreCase("sincronizar")) {
                new SincronizacaoRunnable(getApplicationContext())
                    .addObservador(new NotificacaoObservadora()).run();
            }
        }
    }
}
