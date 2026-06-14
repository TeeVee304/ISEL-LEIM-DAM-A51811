package dam_A51811.filmroulette.data.remote.ai

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.Properties

/**
 * Gemini implementation of [AIAssistant].
 *
 * Builds requests for the Google Generative Language REST API
 * (`generativelanguage.googleapis.com`), exactly as in the tutorial.
 *
 * Model default: **gemini-2.0-flash** (fast and capable).
 */
class AIAssistantGemini(override val properties: Properties) : AIAssistant {

    override fun getSystem() = "GEMINI"
    override val apiKeyName  = "GEMINI_API_KEY"
    override var model       = "gemini-2.0-flash"

    override fun buildRequest(prompt: String): Request {
        val messagesArray = JSONArray().put(
            JSONObject()
                .put("role", "user")
                .put("parts", JSONArray().put(JSONObject().put("text", prompt)))
        )

        val requestBody = JSONObject().put("contents", messagesArray)

        if (temperature != null || maxTokens != null) {
            val genConfig = JSONObject()
            temperature?.let { genConfig.put("temperature", it) }
            maxTokens?.let   { genConfig.put("maxOutputTokens", it) }
            requestBody.put("generationConfig", genConfig)
        }

        return Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1/models/$model:generateContent?key=$apiKey")
            .addHeader("Content-Type", "application/json")
            .post(requestBody.toString().toRequestBody("application/json".toMediaTypeOrNull()))
            .build()
    }
}
