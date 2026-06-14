package dam_A51811.filmroulette.data.remote.ai

import java.util.Properties

/**
 * Creates the appropriate [AIAssistant] implementation based on the
 * `AI_LLM` key in [properties].
 *
 * Supported values: `"GEMINI"` (default), `"NIM"`.
 */
object AIAssistantFactory {
    fun create(properties: Properties): AIAssistant =
        when (properties.getProperty("AI_LLM", "GEMINI").uppercase()) {
            "NIM"    -> AIAssistantNIM(properties)
            else     -> AIAssistantGemini(properties)   // GEMINI is the default
        }
}
