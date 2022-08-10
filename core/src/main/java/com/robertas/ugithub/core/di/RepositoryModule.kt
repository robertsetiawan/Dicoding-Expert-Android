package com.robertas.ugithub.core.di

import com.robertas.ugithub.core.data.source.UserRepository
import com.robertas.ugithub.core.data.source.local.entity.User
import com.robertas.ugithub.core.data.source.remote.response.UserNetwork
import com.robertas.ugithub.core.domain.model.UserDomain
import com.robertas.ugithub.core.domain.repository.IUserRepository
import com.robertas.ugithub.core.utils.mapper.IDataMapper
import com.robertas.ugithub.core.utils.mapper.IMapper
import com.robertas.ugithub.core.utils.mapper.UserEntityMapper
import com.robertas.ugithub.core.utils.mapper.UserNetworkMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindUserNetworkMapper(userNetworkMapper: UserNetworkMapper): IDataMapper<UserNetwork, UserDomain>

    @Binds
    abstract fun bindUserRepository(userRepository: UserRepository): IUserRepository

    @Binds
    abstract fun bindUserEntityMapper(userEntityMapper: UserEntityMapper): IMapper<User, UserDomain>
}