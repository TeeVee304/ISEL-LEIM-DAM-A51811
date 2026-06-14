package dam_A51811.filmroulette.data.remote.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.util.Properties
import kotlin.math.pow

/**
 * Core interface for all AI assistant implementations in FilmRoulette.
 *
 * Inspired by the tutorial's AIAssistant but adapted for Android (no SLF4J,
 * no file-based properties loading) and scoped to film-related features.
 *
 * ### Contract
 * Implementors must provide [buildRequest] to construct the vendor-specific
 * HTTP request. All other methods — retry logic, response parsing and the
 * movie-specific helpers — are implemented here as default methods.
 *
 * ### Firebase note
 * When Firebase is introduced, API keys should be retrieved from
 * Firebase Remote Config instead of the local [properties] object.
 */
interface AIAssistant {

    val properties: Properties
    val logger: AiLogger get() = AiLogger()
    val apiKeyName: String
    var model: String

    val temperature: Double? get() = properties.getProperty("AI_TEMPERATURE")?.toDoubleOrNull()
    val maxTokens: Int?      get() = properties.getProperty("AI_MAX_TOKENS")?.toIntOrNull()

    val client: OkHttpClient get() = OkHttpClient()

    val apiKey: String
        get() = properties.getProperty(apiKeyName)
            ?: throw IllegalStateException("API key '$apiKeyName' not found in config.properties.")

    fun getSystem(): String

    // ─────────────────────────────────────────────────────────────────────────
    // Vendor-specific hook
    // ─────────────────────────────────────────────────────────────────────────

    /** Builds the vendor-specific HTTP request for the given [prompt]. */
    fun buildRequest(prompt: String): Request

    // ─────────────────────────────────────────────────────────────────────────
    // HTTP + retry logic  (identical pattern to the tutorial)
    // ─────────────────────────────────────────────────────────────────────────

    /** Calls the API with exponential backoff on HTTP 429. */
    suspend fun apiCallWithBackoff(prompt: String): String {
        var attempts   = 0
        val maxAttempts = 5
        val baseDelay   = 1_000L

        while (attempts < maxAttempts) {
            try {
                return makeApiCall(prompt)
            } catch (e: Exception) {
                logger.error("AI call error: ${e.message}")
                if (e.message?.contains("429") == true) {
                    attempts++
                    val delayMs = baseDelay * (2.0.pow(attempts.toDouble())).toLong()
                    logger.warn("Rate-limited (429). Retry $attempts in ${delayMs}ms.")
                    delay(delayMs)
                } else {
                    throw e
                }
            }
        }
        throw Exception("Exceeded maximum AI retry attempts.")
    }

    /**
     * Makes a single HTTP call and parses the **Gemini-style** JSON response.
     * NIM overrides this to parse the OpenAI-style `choices[0].message.content`.
     */
    fun makeApiCall(prompt: String): String {
        logger.info("AI prompt (first 200 chars): ${prompt.take(200)}")
        val request = buildRequest(prompt)

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                val body = response.body?.string()
                throw Exception("AI API error ${response.code}: ${response.message}\n$body")
            }

            val responseBody = response.body?.string() ?: return "Error: empty response"

            return try {
                val json       = JSONObject(responseBody)
                val candidates = json.getJSONArray("candidates")
                val content    = candidates.getJSONObject(0).getJSONObject("content")
                val text       = content.getJSONArray("parts").getJSONObject(0).getString("text")
                text.trim()
            } catch (e: JSONException) {
                logger.error("Failed to parse AI JSON: ${e.message}")
                throw Exception("Failed to parse AI response: ${e.message}", e)
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Film-specific AI features
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Generates a rich description of a film by its [title].
     *
     * Returns a structured text with:
     * - **Plot overview** (no spoilers)
     * - **Key themes**
     * - **Trivia / did-you-know**
     * - **Who should watch** (audience fit)
     */
    suspend fun describeMovie(title: String): String = withContext(Dispatchers.IO) {
        val prompt = """
            You are a knowledgeable and enthusiastic film critic assistant.
            The user wants to know more about the movie: "$title".

            Please respond in clear English with EXACTLY these four labelled sections:

            🎬 PLOT OVERVIEW
            (A concise, spoiler-free summary of the story in 3–4 sentences.)

            🎭 KEY THEMES
            (2–4 major themes explored in the film, as a short bullet list.)

            💡 DID YOU KNOW?
            (One genuinely interesting behind-the-scenes fact or trivia.)

            👥 WHO SHOULD WATCH
            (A single sentence describing the ideal audience for this film.)

            Keep the total response under 300 words. Do not add any other sections or headings.
        """.trimIndent()

        apiCallWithBackoff(prompt)
    }

    /**
     * Generates 3 personalised movie recommendations based on a free-text
     * description of the user's [mood] or preferences.
     *
     * Returns a structured text with exactly 3 numbered recommendations,
     * each with a title, one-line reason and a genre tag.
     */
    suspend fun recommendMovies(mood: String): String = withContext(Dispatchers.IO) {
        val prompt = """
            You are a passionate film curator assistant for the FilmRoulette app.
            The user is looking for a movie and describes their mood or preference as:
            "$mood"

            Suggest EXACTLY 3 movies that fit perfectly. For each movie, use this exact format:

            1. 🎬 [MOVIE TITLE] ([YEAR])
               Genre: [Genre tags]
               Why: [One sentence explaining why this film fits the user's mood.]

            2. 🎬 [MOVIE TITLE] ([YEAR])
               Genre: [Genre tags]
               Why: [One sentence explaining why this film fits the user's mood.]

            3. 🎬 [MOVIE TITLE] ([YEAR])
               Genre: [Genre tags]
               Why: [One sentence explaining why this film fits the user's mood.]

            Keep the total response concise. Do not add any introduction or conclusion.
        """.trimIndent()

        apiCallWithBackoff(prompt)
    }
}
