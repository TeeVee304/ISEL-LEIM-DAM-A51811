package dam

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * AIAssistantNIM class provides an interface to communicate with a self-hosted NVIDIA NIM.
 * This class handles request formatting, response parsing, and error handling for OpenAI-compatible APIs.
 *
 * @param properties Properties containing the endpoint and model configuration for NIM
 */
class AIAssistantNIM(override val properties: Properties) : AIAssistant {

    override fun getSystem() = "NIM"
    override val apiKeyName = "NIM_API_KEY"

    override var model: String = properties.getProperty("NIM_MODEL", "google/gemma-2-2b-it")
    
    private val endpoint: String = properties.getProperty("NIM_ENDPOINT", "https://integrate.api.nvidia.com/v1/chat/completions")

    /**
     * Constructs and formats a structured request from the given input prompt.
     * This method prepares the necessary request structure for sending to an OpenAI-compatible API.
     *
     * @param prompt The user's input query or prompt that needs to be formatted into a request
     */
    override fun buildRequest(prompt: String): Request {
        // Create the message array with user content only
        // Note: Gemma-2 via NVIDIA API does not support the "system" role.
        val messagesArray = JSONArray()
            .put(
                JSONObject()
                    .put("role", "user")
                    .put("content", prompt)
            )

        // Build the complete request body with model selection and messages
        val requestBody = JSONObject()
            .put("model", model)
            .put("messages", messagesArray)

        // Add optional parameters if defined
        temperature?.let { requestBody.put("temperature", it) }
        maxTokens?.let { requestBody.put("max_tokens", it) }
        
        // Add recommended top_p parameter
        requestBody.put("top_p", 0.7)

        val requestBodyString = requestBody.toString()

        // Configure the HTTP request
        val builder = Request.Builder()
            .url(endpoint)
            .addHeader("Content-Type", "application/json")
            .post(requestBodyString.toRequestBody("application/json".toMediaTypeOrNull()))

        // Add API key if present
        try {
            val key = apiKey
            if (key.isNotEmpty()) {
                builder.addHeader("Authorization", "Bearer $key")
            }
        } catch (e: Exception) {
            // If API key is not found, we continue without it (might not be needed for local NIM)
            logger.warn("No API key found for NIM, proceeding without Authorization header.")
        }

        return builder.build()
    }

    /**
     * Overrides makeApiCall to handle the OpenAI/NIM response format.
     * The default implementation in AIAssistant is hardcoded for Gemini.
     */
    override fun makeApiCall(prompt: String): String {
        logger.info("NIM Prompt:\n$prompt")

        val request = buildRequest(prompt)

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                val errorBody = response.body?.string()
                throw Exception("Error in NIM API call: ${response.code} - ${response.message}\nResponse: $errorBody")
            }

            val responseBody = response.body?.string() ?: return "Error: empty response"

            try {
                val json = JSONObject(responseBody)
                logger.debug("Raw NIM response: {}", responseBody)

                if (!json.has("choices") || json.getJSONArray("choices").length() == 0) {
                    return "Error: No choices found in the NIM response"
                }

                val choices = json.getJSONArray("choices")
                val firstChoice = choices.getJSONObject(0)

                if (!firstChoice.has("message")) {
                    return "Error: No message found in the NIM response choice"
                }

                val message = firstChoice.getJSONObject("message")
                
                if (!message.has("content")) {
                    return "Error: No content found in the NIM message"
                }

                return message.getString("content").trim()

            } catch (e: JSONException) {
                val truncatedResponse = if (responseBody.length > 200)
                    "${responseBody.substring(0, 200)}..."
                else
                    responseBody

                logger.error("Error parsing NIM JSON response: ${e.message}")
                logger.error("Response body (truncated): $truncatedResponse")

                throw Exception("Failed to parse NIM API response: ${e.message}", e)
            }
        }
    }
}
