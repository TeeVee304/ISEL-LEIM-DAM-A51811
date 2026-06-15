package dam_A51811.filmroulette.data.remote.ai

import android.util.Log


/**
 * Provides logging capabilities tailored for AI-related operations.
 *
 * @param tag The tag used for logging. Defaults to "FilmRoulette.AI".
 */
class AiLogger(private val tag: String = "FilmRoulette.AI") {
    /**
     * Logs an error message, optionally with an associated [Throwable].
     *
     * @param msg The error message to log.
     * @param t An optional [Throwable] that caused the error.
     */
    fun error(msg: String, t: Throwable? = null) = Log.e(tag, msg, t)
    /**
     * Logs a warning message.
     *
     * @param msg The warning message to log.
     */
    fun warn(msg: String)                         = Log.w(tag, msg)
    /**
     * Logs an informational message.
     *
     * @param msg The informational message to log.
     */
    fun info(msg: String)                         = Log.i(tag, msg)
    /**
     * Logs a debug message, with an optional argument to replace in the format string.
     *
     * @param msg The debug message to log. Occurrences of "{}" will be replaced by the string representation of [arg].
     * @param arg An optional argument to replace in the message.
     */
    fun debug(msg: String, arg: Any? = null) {
        Log.d(tag, if (arg != null) msg.replace("{}", arg.toString()) else msg)
    }
}
