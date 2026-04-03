package run.simple.interval_timer

import android.app.Application
import android.os.StrictMode
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import run.simple.interval_timer.di.appModule
import timber.log.Timber

class IntervalTimerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupStrictMode()
        initLogger()
        initDI()
    }

    private fun setupStrictMode() {
        if (BuildConfig.DEBUG) {
            // Политика для потоков (Thread Policy)
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()        // Логировать нарушения
                    .penaltyFlashScreen() // Мигать экраном при нарушении
                    .build()
            )

            // Политика для VM (утечки памяти и т.д.)
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedClosableObjects()
                    .detectLeakedSqlLiteObjects()
                    .detectActivityLeaks()
                    .penaltyLog()
                    .build()
            )
        }
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(
                tree = Timber.DebugTree()
            )
        }
    }

    private fun initDI() {
        startKoin {
            androidContext(this@IntervalTimerApplication)
            modules(
                listOf(
                    appModule,
                )
            )
        }
    }
}