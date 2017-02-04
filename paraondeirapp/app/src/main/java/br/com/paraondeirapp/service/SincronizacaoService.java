package br.com.paraondeirapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import br.com.paraondeirapp.job.JobSincronizacao;

public class SincronizacaoService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
/*      Calendar agora = Calendar.getInstance();
        Calendar meiaNoite = Calendar.getInstance();
        meiaNoite.set(Calendar.HOUR_OF_DAY, 23);
        meiaNoite.set(Calendar.MINUTE, 59);
        meiaNoite.set(Calendar.SECOND, 59);
        long difBase = meiaNoite.getTimeInMillis() - agora.getTimeInMillis();
        long delayInicial = difBase / (60 * 1000) % 60; //Converter diferença em minutos

        ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1); //threads no pool
        NotificacaoObservadora observer = new NotificacaoObservadora();

        pool.scheduleAtFixedRate(new SincronizacaoRunnable(getApplicationContext()).addObservador(observer),
                delayInicial, 1380, TimeUnit.MINUTES); //Runnable / delay inicio / frequencia / tipo contagem tempo.
*/

        // Personaliza execução automática da sincronização usando Quartz.
        SchedulerFactory factory = new StdSchedulerFactory();
        try {
            Scheduler scheduler = factory.getScheduler();
            JobDetail jobDetail = JobBuilder.newJob(JobSincronizacao.class)
                    .withIdentity("sincronizacao", "sinc")
                    .build();
            Trigger job = TriggerBuilder.newTrigger()
                    .withIdentity("sincronizacao_automatica", "sinc") //nome trigger
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 1 * * * *")) // frequência de execução (Sempre 1 da manhã)
                    .build();
            scheduler.scheduleJob(jobDetail, job); //Adiciona na lista
            Log.i("AppParaOndeIr.onCreate()", "Job de sincronização registrado.");
            scheduler.start(); //inicializa varredura da lista.
        } catch (SchedulerException ex){
            Log.i("AppParaOndeIr.onCreate()", ex.getMessage());
        }
        return START_STICKY;
    }
}
