package com.robertas.ugithub.core.utils.mapper

import com.robertas.ugithub.core.data.source.remote.response.UserNetwork
import com.robertas.ugithub.core.domain.model.UserDomain
import javax.inject.Inject

class UserNetworkMapper @Inject constructor(): IDataMapper<UserNetwork, UserDomain> {
    override fun mapFromData(source: UserNetwork): UserDomain {
        return UserDomain(
            login = source.login ?: "",
            bio = source.bio ?: "",
            company = source.company ?: "",
            id = source.id ?: 0,
            publicRepos = source.publicRepos ?: 0,
            followers = source.followers ?: 0,
            avatarUrl = source.avatarUrl ?: "",
            blog = source.blog ?: "",
            following = source.following ?: 0,
            name = source.name ?: "",
            location = source.location ?: ""
        )
    }
}