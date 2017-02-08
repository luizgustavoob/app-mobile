package br.com.paraondeirapp.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import br.com.paraondeirapp.R;

public class NotificacaoUtils {

    public static void notificar(int idNotificacao, Context ctx, PendingIntent intent,
                                 String ticker, String titulo, String texto){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);

        builder.setTicker(ticker)
                .setContentTitle(titulo)
                .setContentText(texto)
                .setSmallIcon(R.drawable.ic_notificacao)
                .setContentIntent(intent)
                .setAutoCancel(false);

        Notification n = builder.build();
        NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(idNotificacao, n);
    }
}