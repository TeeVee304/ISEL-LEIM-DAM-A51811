package dam_A51811.filmroulette.data.model


/**
 * Represents the visibility levels for user-generated content or profiles.
 */
enum class Visibility {
    /**
     * Content is visible only to the owner.
     */
    PRIVATE,

    /**
     * Content is visible only to authenticated friends of the owner.
     */
    FRIENDS_ONLY,

    /**
     * Content is visible to all users.
     */
    PUBLIC
}
