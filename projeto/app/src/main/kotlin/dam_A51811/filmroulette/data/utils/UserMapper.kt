package dam_A51811.filmroulette.data.utils

import dam_A51811.filmroulette.data.local.User as LocalUser
import dam_A51811.filmroulette.data.model.User as DomainUser


/**
 * Mapper object for user entity conversions.
 */
object UserMapper {
    /**
     * Converts a local database user into a domain user.
     *
     * @return A mapped domain user object.
     */
    fun LocalUser.toDomainUser(): DomainUser = DomainUser(
        id           = this.id.toString(),
        username     = this.username,
        email        = this.email,
        registryDate = this.registryDate,
        avatarUrl    = this.avatarUrl
    )
}
