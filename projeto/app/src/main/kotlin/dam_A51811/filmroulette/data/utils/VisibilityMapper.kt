package dam_A51811.filmroulette.data.utils

import dam_A51811.filmroulette.data.model.Visibility

/**
 * Converts between the domain [Visibility] enum and the raw String stored
 * in the Room database (and eventually Firestore).
 *
 * Using a plain String column — instead of a TypeConverter on the enum — keeps
 * the database schema readable and makes Firebase integration straightforward.
 *
 * ### Firebase note
 * Firestore documents will store the same String values
 * ("PRIVATE", "FRIENDS_ONLY", "PUBLIC"). Reuse [fromString] when reading
 * Firestore documents to obtain the domain enum.
 */
object VisibilityMapper {

    /** Converts a [Visibility] enum value to the database/Firestore String. */
    fun toString(visibility: Visibility): String = visibility.name

    /**
     * Converts a raw String from the database/Firestore back to a [Visibility].
     *
     * Falls back to [Visibility.PRIVATE] for unknown/missing values to avoid
     * accidental data exposure.
     */
    fun fromString(value: String?): Visibility =
        when (value) {
            "PUBLIC"       -> Visibility.PUBLIC
            "FRIENDS_ONLY" -> Visibility.FRIENDS_ONLY
            else           -> Visibility.PRIVATE   // safe default
        }
}
