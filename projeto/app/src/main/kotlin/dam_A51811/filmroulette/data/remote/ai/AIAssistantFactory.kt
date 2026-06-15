package dam_A51811.filmroulette.data.remote.ai

import java.util.Properties


/**
 * Factory object for creating [AIAssistant] instances based on configuration properties.
 */
object AIAssistantFactory {
    /**
     * Creates and returns a concrete [AIAssistant] implementation.
     *
     * @param properties The configuration properties containing the selected AI provider.
     * @return A configured [AIAssistant] instance.
     */
    fun create(properties: Properties): AIAssistant =
        when (properties.getProperty("AI_LLM", "GEMINI").uppercase()) {
            "NIM"    -> AIAssistantNIM(properties)
            else     -> AIAssistantGemini(properties)   
        }
}
