package run.simple.feature_training_screen.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.SoundPool
import androidx.annotation.RawRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import run.simple.feature_training_screen.R
import kotlin.coroutines.resume

/**
 * Проигрывает короткие сигналы интервального таймера, корректно сосуществуя
 * с фоновым аудио (музыка, подкасты, радио) — на время сигнала чужое аудио
 * приглушается ("ducking"), затем громкость возвращается.
 *
 * Правила использования:
 *  - создать один экземпляр на жизненный цикл экрана/ViewModel;
 *  - вызывать [playStart], [playTransition], [playFinish] из главного потока;
 *  - обязательно вызвать [release] в onCleared() / onDestroy().
 */
class TimerSoundPlayer(context: Context) {

    private val appContext = context.applicationContext
    private val audioManager =
        appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    // Атрибуты звука: SONIFICATION + USAGE_ASSISTANCE_SONIFICATION
    // — система понимает, что это короткий вспомогательный сигнал, а не музыка,
    // и применяет к нему правильную политику смешивания с другим аудио.
    private val audioAttributes: AudioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(2)
        .setAudioAttributes(audioAttributes)
        .build()

    private val loadedSounds = mutableSetOf<Int>()

    private val startId: Int = soundPool.load(appContext, R.raw.beep_start, 1)
    private val transitionId: Int = soundPool.load(appContext, R.raw.beep_transition, 1)
    private val finishId: Int = soundPool.load(appContext, R.raw.beep_finish, 1)
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // Текущий focus-запрос
    private var focusRequest: AudioFocusRequest? = null

    fun prefetch() {
        scope.launch(Dispatchers.IO) {
            soundPool.loadAndAwait(appContext, R.raw.beep_start)
            soundPool.loadAndAwait(appContext, R.raw.beep_transition)
            soundPool.loadAndAwait(appContext, R.raw.beep_finish)
        }
    }

    fun playStart() = playWithFocus {
        play(startId)
    }

    fun playTransition() = playWithFocus {
        play(transitionId)
    }

    fun playFinish() {
        scope.launch {
            val granted = requestFocus()
            if (!granted) return@launch
            try {
                play(finishId)
                delay(5_000)
            } finally {
                abandonFocus()
            }
        }
    }

    private fun play(soundId: Int) {
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    /**
     * Одноразовый сигнал с автоматическим возвратом фокуса
     * через ~500 мс — за это время короткий beep успевает прозвучать.
     */
    private fun playWithFocus(play: () -> Unit) {
        scope.launch {
            val granted = requestFocus()
            if (!granted) return@launch
            try {
                play()
                delay(500)
            } finally {
                abandonFocus()
            }
        }
    }

    // ---------- Audio Focus ----------

    /**
     * Запрашивает кратковременный audio focus с возможностью "ducking".
     * Это просит систему приглушить (а не остановить) чужое аудио на время
     * нашего сигнала. Для подкастов/аудиокниг система может вместо ducking
     * сделать pause — это решает не приложение, а сам плеер по своим правилам.
     */
    private fun requestFocus(): Boolean {
        val request = AudioFocusRequest.Builder(
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
        )
            .setAudioAttributes(audioAttributes)
            .setWillPauseWhenDucked(false)
            // Сигналы короткие — обработчик потери фокуса нам не нужен,
            // но передать его обязательно.
            .setOnAudioFocusChangeListener { /* no-op */ }
            .build()
        focusRequest = request
        val result = audioManager.requestAudioFocus(request)
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    private fun abandonFocus() {
        focusRequest?.let { audioManager.abandonAudioFocusRequest(it) }
        focusRequest = null
    }

    fun release() {
        scope.cancel()
        abandonFocus()
        soundPool.release()
    }
}

/**
 * Загружаем асинхронно ресурс
 * */
private suspend fun SoundPool.loadAndAwait(context: Context, @RawRes resId: Int): Int =
    suspendCancellableCoroutine { cont ->
        val id = load(context, resId, 1)
        setOnLoadCompleteListener { _, sampleId, status ->
            if (sampleId == id) {
                if (status == 0) cont.resume(id)
                else cont.resume(-1)
            }
        }
    }