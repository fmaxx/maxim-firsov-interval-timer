package run.simple.feature_training_screen.sound

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import org.koin.android.ext.android.inject
import run.simple.feature_training_screen.R
import timber.log.Timber

interface TrainingServiceInteractor {

    fun run(cmd: TrainingServiceCommand)
}

sealed interface TrainingServiceCommand {
    data object PlayStart : TrainingServiceCommand
    data object PlayInterval : TrainingServiceCommand
    data object PlayFinish : TrainingServiceCommand
}

class TrainingService : TrainingServiceInteractor, Service() {

    private val binder = TrainingServiceBinder()
    private val player: TimerSoundPlayer by inject()

    inner class TrainingServiceBinder : Binder() {

        fun getService(): TrainingServiceInteractor = this@TrainingService
    }

    init {
        Timber.d("~~~ init")
        player.prefetch()
    }

    override fun run(cmd: TrainingServiceCommand) {
        when (cmd) {
            TrainingServiceCommand.PlayStart -> player.playStart()
            TrainingServiceCommand.PlayInterval -> player.playTransition()
            TrainingServiceCommand.PlayFinish -> player.playFinish()
        }
    }

    override fun onBind(intent: Intent): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = buildNotification("Тренировка идёт", "Готово к старту")
        startForeground(NOTIFICATION_ID, notification)
        return START_STICKY
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            /* id = */ CHANNEL_ID,
            /* name = */ "Тренировка",
            /* importance = */ NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }

    private fun buildNotification(title: String, text: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_timer)
            .setOngoing(true)
            .setSilent(true) // нам не нужно дёргать notification sound при каждом обновлении
            .build()
    }

    companion object {

        const val CHANNEL_ID = "interval_timer_channel"
        const val NOTIFICATION_ID = 1001

        fun start(context: Context) {
            val intent = Intent(context, TrainingService::class.java)
            context.startForegroundService(intent)
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, TrainingService::class.java))
        }
    }
}