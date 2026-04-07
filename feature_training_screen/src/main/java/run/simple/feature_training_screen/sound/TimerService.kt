package run.simple.feature_training_screen.sound

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import run.simple.feature_training_screen.R

class TimerService : Service() {

    private val binder = TimerBinder()
    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onBind(intent: Intent): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = buildNotification("Тренировка идёт", "Готово к старту")
        startForeground(NOTIFICATION_ID, notification)
        return START_STICKY
    }

    fun updateNotification(text: String) {
        val notification = buildNotification("Тренировка идёт", text)
        getSystemService(NotificationManager::class.java)
            .notify(NOTIFICATION_ID, notification)
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
        const val CHANNEL_ID = "interval_timer"
        const val NOTIFICATION_ID = 1
    }
}