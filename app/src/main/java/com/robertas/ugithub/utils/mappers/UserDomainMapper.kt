package com.robertas.ugithub.utils.mappers

import com.robertas.ugithub.abstractions.IEntityMapper
import com.robertas.ugithub.models.domain.UserDomain
import com.robertas.ugithub.models.network.UserNetwork
import javax.inject.Inject

class UserDomainMapper @Inject constructor() : IEntityMapper<UserDomain, UserNetwork> {

    override fun mapToEntity(target: UserNetwork): UserDomain {
        return UserDomain(
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

    override fun mapFromEntity(source: UserDomain): UserNetwork = UserNetwork()
}