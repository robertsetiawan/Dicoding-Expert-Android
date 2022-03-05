package com.robertas.ugithub.utils.mappers

import com.robertas.ugithub.interfaces.IEntityMapper
import com.robertas.ugithub.models.domain.User
import com.robertas.ugithub.models.network.UserNetwork

class UserMapper : IEntityMapper<User, UserNetwork> {

    override fun maptoEntity(target: UserNetwork): User {
        return User(
            login = target.login ?: "",
            bio = target.bio ?: "",
            company = target.company ?: "",
            id = target.id ?: 0,
            publicRepos = target.publicRepos ?: 0,
            followers = target.followers ?: 0,
            avatarUrl = target.avatarUrl ?: "",
            blog = target.blog ?: "",
            following = target.following ?: 0,
            name = target.name ?: "",
            location = target.location ?: ""
        )
    }
}