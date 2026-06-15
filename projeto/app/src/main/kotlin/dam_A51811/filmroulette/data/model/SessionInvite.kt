package dam_A51811.filmroulette.data.model

/**
 * Represents an invitation to join a group session.
 *
 * @param sessionId The unique identifier of the session the user is being invited to.
 * @param inviterName The display name of the user who sent the invitation.
 * @param inviterAvatarUrl The URL of the inviter's avatar image, or null if unavailable.
 */
data class SessionInvite(
    val sessionId: String = "",
    val inviterName: String = "",
    val inviterAvatarUrl: String? = null
)
