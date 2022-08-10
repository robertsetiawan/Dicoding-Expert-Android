package com.robertas.ugithub.core.utils.mapper

import com.robertas.ugithub.core.domain.model.UserDomain
import com.robertas.ugithub.core.data.source.local.entity.User
import javax.inject.Inject

class UserEntityMapper @Inject constructor() : IMapper<User, UserDomain> {
    override fun mapFromDomain(source: UserDomain): User {
        return User(
            login = source.login,
            id = source.id,
            avatarUrl = source.avatarUrl,
        )
    }

    override fun mapFromData(source: User): UserDomain {
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