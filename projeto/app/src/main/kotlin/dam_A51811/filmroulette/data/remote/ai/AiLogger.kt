package dam_A51811.filmroulette.data.remote.ai

import android.util.Log

/**
 * Android-compatible logger that replaces SLF4J (which is not natively
 * available on Android) with calls to [android.util.Log].
 *
 * Used internally by [AIAssistant] and its implementations.
 */
class AiLogger(private val tag: String = "FilmRoulette.AI") {
    fun error(msg: String, t: Throwable? = null) = Log.e(tag, msg, t)
    fun warn(msg: String)                         = Log.w(tag, msg)
    fun info(msg: String)                         = Log.i(tag, msg)
    fun debug(msg: String, arg: Any? = null) {
        Log.d(tag, if (arg != null) msg.replace("{}", arg.toString()) else msg)
    }
}
