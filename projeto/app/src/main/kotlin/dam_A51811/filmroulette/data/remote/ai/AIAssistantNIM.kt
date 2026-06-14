package dam_A51811.filmroulette.data.remote.ai

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.Properties

/**
 * NVIDIA NIM (OpenAI-compatible) implementation of [AIAssistant].
 *
 * Overrides [makeApiCall] to parse the OpenAI `choices[0].message.content`
 * response format instead of the Gemini `candidates[0].content.parts[0].text` format.
 */
class AIAssistantNIM(override val properties: Properties) : AIAssistant {

    override fun getSystem() = "NIM"
    override val apiKeyName  = "NIM_API_KEY"
    override var model: String = properties.getProperty("NIM_MODEL", "google/gemma-2-2b-it")

    private val endpoint: String =
        properties.getProperty("NIM_ENDPOINT", "https://integrate.api.nvidia.com/v1/chat/completions")

    override fun buildRequest(prompt: String): Request {
        val messagesArray = JSONArray().put(
            JSONObject().put("role", "user").put("content", prompt)
        )

        val body = JSONObject()
            .put("model", model)
            .put("messages", messagesArray)
            .put("top_p", 0.7)

        temperature?.let { body.put("temperature", it) }
        maxTokens?.let   { body.put("max_tokens", it) }

        val builder = Request.Builder()
            .url(endpoint)
            .addHeader("Content-Type", "application/json")
            .post(body.toString().toRequestBody("application/json".toMediaTypeOrNull()))

        try { builder.addHeader("Authorization", "Bearer $apiKey") }
        catch (e: Exception) { logger.warn("No NIM API key found, proceeding without auth.") }

        return builder.build()
    }

    /** Parses the OpenAI-style response format used by NIM. */
    override fun makeApiCall(prompt: String): String {
        logger.info("NIM prompt (first 200 chars): ${prompt.take(200)}")
        val request = buildRequest(prompt)

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                val body = response.body?.string()
                throw Exception("NIM API error ${response.code}: ${response.message}\n$body")
            }

            val responseBody = response.body?.string() ?: return "Error: empty response"

            return try {
                val json    = JSONObject(responseBody)
                val choices = json.getJSONArray("choices")
                choices.getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim()
            } catch (e: JSONException) {
                logger.error("Failed to parse NIM JSON: ${e.message}")
                throw Exception("Failed to parse NIM response: ${e.message}", e)
            }
        }
    }
}
