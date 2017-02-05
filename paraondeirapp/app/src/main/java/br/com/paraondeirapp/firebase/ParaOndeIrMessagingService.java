package br.com.paraondeirapp.firebase;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import br.com.paraondeirapp.observer.NotificacaoObservadora;

public class ParaOndeIrMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notificacao = remoteMessage.getNotification();

        if (notificacao.getBody().trim().equalsIgnoreCase("sincronizar")) {
            SincronizacaoRunnable sinc = new SincronizacaoRunnable(this)
                    .addObservador(new NotificacaoObservadora());
            sinc.run();
        }
    }
}
