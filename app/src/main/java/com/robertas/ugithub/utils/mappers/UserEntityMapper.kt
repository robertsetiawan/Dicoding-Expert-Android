package com.robertas.ugithub.utils.mappers

import com.robertas.ugithub.abstractions.IEntityMapper
import com.robertas.ugithub.models.domain.UserDomain
import com.robertas.ugithub.models.entity.User
import javax.inject.Inject

class UserEntityMapper @Inject constructor() : IEntityMapper<User, UserDomain> {
    override fun mapToEntity(target: UserDomain): User {
        return User(
            login = target.login,
            id = target.id,
            avatarUrl = target.avatarUrl,
        )
    }

    override fun mapFromEntity(source: User): UserDomain {
        return UserDomain(
            login = source.login,
            bio = "",
            company = "",
            id = source.id,
            publicRepos = 0,
            followers = 0,
            avatarUrl = source.avatarUrl,
            blog = "",
            following = 0,
            name = "",
            location = ""
        )
    }
}