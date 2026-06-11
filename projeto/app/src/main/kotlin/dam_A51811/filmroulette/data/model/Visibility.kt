package dam_A51811.filmroulette.data.model

/**
 * Defines who can see a [MovieList] (watchlist).
 *
 * | Level        | Who can see it                              |
 * |--------------|---------------------------------------------|
 * | PRIVATE      | Only the owner of the list                  |
 * | FRIENDS_ONLY | The owner + accepted bilateral friends      |
 * | PUBLIC       | Everyone, including non-friends             |
 *
 * This enum is used in the domain layer and mapped to/from a String
 * column in the Room database via [dam_A51811.filmroulette.data.utils.VisibilityMapper].
 *
 * Future note: when Firebase is introduced, store this value as a
 * plain string field in Firestore (e.g. "PRIVATE", "FRIENDS_ONLY",
 * "PUBLIC") and parse it back using [VisibilityMapper.fromString].
 */
enum class Visibility {
    PRIVATE,
    FRIENDS_ONLY,
    PUBLIC
}
