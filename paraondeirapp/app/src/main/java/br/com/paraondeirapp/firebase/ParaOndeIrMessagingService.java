package br.com.paraondeirapp.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import br.com.paraondeirapp.observer.impl.NotificacaoObservadora;

public class ParaOndeIrMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        /*RemoteMessage.Notification notificacao = remoteMessage.getNotification();

        if (notificacao.getBody().trim().equalsIgnoreCase("sincronizar")) {
            SincronizacaoRunnable sinc = new SincronizacaoRunnable(this)
                    .addObservador(new NotificacaoObservadora());
            sinc.run();
        }*/

        Log.i("token", "Recebeu a mensagem de notificação");
        Map<String, String> map = remoteMessage.getData();
        if (map.containsKey("sincronizar")) {
            if (map.get("sincronizar").trim().equalsIgnoreCase("sincronizar")) {
                SincronizacaoRunnable sinc = new SincronizacaoRunnable(this)
                        .addObservador(new NotificacaoObservadora());
                sinc.run();
            }
        }
    }
}
