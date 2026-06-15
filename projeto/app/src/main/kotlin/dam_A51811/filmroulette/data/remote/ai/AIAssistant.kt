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
 * Represents an artificial intelligence assistant capable of processing user prompts.
 */
interface AIAssistant {

    /**
     * The configuration properties for the AI assistant.
     */
    val properties: Properties
    /**
     * The logger instance used for recording AI-related events.
     */
    val logger: AiLogger get() = AiLogger()
    /**
     * The property key name used to retrieve the API key.
     */
    val apiKeyName: String
    /**
     * The model identifier used for generating responses.
     */
    var model: String

    /**
     * The optional temperature parameter for controlling response randomness.
     */
    val temperature: Double? get() = properties.getProperty("AI_TEMPERATURE")?.toDoubleOrNull()
    /**
     * The optional maximum number of tokens allowed in the response.
     */
    val maxTokens: Int?      get() = properties.getProperty("AI_MAX_TOKENS")?.toIntOrNull()

    /**
     * The HTTP client used for making API requests.
     */
    val client: OkHttpClient get() = OkHttpClient()

    /**
     * The retrieved API key.
     *
     * @throws IllegalStateException If the API key is not found in the configuration properties.
     */
    val apiKey: String
        get() = properties.getProperty(apiKeyName)
            ?: throw IllegalStateException("API key '$apiKeyName' not found in config.properties.")

    /**
     * Retrieves the system prompt configuration.
     *
     * @return The system prompt string.
     */
    fun getSystem(): String

    
    
    

    
    /**
     * Builds the HTTP request for a given prompt.
     *
     * @param prompt The user's input prompt.
     * @return The constructed HTTP [Request].
     */
    fun buildRequest(prompt: String): Request

    
    
    

    
    /**
     * Executes an API call with exponential backoff for handling rate limits.
     *
     * @param prompt The user's input prompt.
     * @return The raw response string from the AI API.
     * @throws Exception If the maximum number of retry attempts is exceeded or another error occurs.
     */
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
     * Parses the raw response body into a clean output string.
     *
     * @param responseBody The raw response payload.
     * @return The parsed content string.
     */
    fun parseResponse(responseBody: String): String

    
    /**
     * Makes a synchronous API call to the AI service.
     *
     * @param prompt The user's input prompt.
     * @return The parsed AI response.
     * @throws Exception If the HTTP request fails or the response is empty.
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

            return parseResponse(responseBody)
        }
    }

    
    
    

    
    /**
     * Generates a descriptive overview of a specific movie.
     *
     * @param title The title of the movie to describe.
     * @return A formatted string containing the movie's plot overview, key themes, trivia, and target audience.
     */
    suspend fun describeMovie(title: String, language: String = java.util.Locale.getDefault().displayLanguage): String = withContext(Dispatchers.IO) {
        val prompt = """
            You are a knowledgeable and enthusiastic film critic assistant.
            The user wants to know more about the movie: "$title".

            Please respond in $language with EXACTLY these four labelled sections (translate the section titles to $language too):

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
     * Recommends movies based on the user's specified mood.
     *
     * @param mood A description of the user's current mood or preferences.
     * @return A formatted string listing recommended movies and the reasoning for each.
     */
    suspend fun recommendMovies(mood: String, language: String = java.util.Locale.getDefault().displayLanguage): String = withContext(Dispatchers.IO) {
        val prompt = """
            You are a passionate film curator assistant for the FilmRoulette app.
            The user is looking for a movie and describes their mood or preference as:
            "$mood"

            Suggest EXACTLY 3 movies that fit perfectly. Please respond in $language. For each movie, use this exact format (translate the section titles to $language too):

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
