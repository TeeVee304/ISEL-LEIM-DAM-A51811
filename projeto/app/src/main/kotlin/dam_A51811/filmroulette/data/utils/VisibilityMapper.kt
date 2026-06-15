package dam_A51811.filmroulette.data.utils

import dam_A51811.filmroulette.data.model.Visibility


/**
 * Mapper object for visibility state conversions.
 */
object VisibilityMapper {

    
    /**
     * Converts a visibility enum to its string representation.
     *
     * @param visibility The visibility enum to convert.
     * @return The string representation of the given visibility state.
     */
    fun toString(visibility: Visibility): String = visibility.name

    
    /**
     * Converts a string value to a visibility enum.
     *
     * @param value The string representation of the visibility state.
     * @return The corresponding visibility enum, defaulting to PRIVATE if the value is unrecognized.
     */
    fun fromString(value: String?): Visibility =
        when (value) {
            "PUBLIC"       -> Visibility.PUBLIC
            "FRIENDS_ONLY" -> Visibility.FRIENDS_ONLY
            else           -> Visibility.PRIVATE   
        }
}
