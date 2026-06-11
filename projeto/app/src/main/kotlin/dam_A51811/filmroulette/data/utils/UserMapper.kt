package dam_A51811.filmroulette.data.utils

import dam_A51811.filmroulette.data.local.User as LocalUser
import dam_A51811.filmroulette.data.model.User as DomainUser

/**
 * Extension functions to convert between the local Room [LocalUser] entity
 * and the domain [DomainUser] model.
 *
 * ### Firebase note
 * When reading a Firestore `UserDocument`, map its fields to [DomainUser] directly
 * (bypassing the local entity) using the same field names.
 */
object UserMapper {
    fun LocalUser.toDomainUser(): DomainUser = DomainUser(
        id           = this.id,
        username     = this.username,
        email        = this.email,
        registryDate = this.registryDate
    )
}
