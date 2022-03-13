package com.robertas.ugithub.modules

import com.robertas.ugithub.abstractions.IEntityMapper
import com.robertas.ugithub.abstractions.IRepository
import com.robertas.ugithub.models.domain.UserDomain
import com.robertas.ugithub.models.entity.User
import com.robertas.ugithub.models.network.UserNetwork
import com.robertas.ugithub.repositories.UserRepository
import com.robertas.ugithub.utils.mappers.UserDomainMapper
import com.robertas.ugithub.utils.mappers.UserEntityMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindUserMapper(userDomainMapper: UserDomainMapper): IEntityMapper<UserDomain, UserNetwork>

    @Binds
    abstract fun bindUserRepository(userRepository: UserRepository): IRepository<UserDomain>

    @Binds
    abstract fun bindUserEntityMapper(userEntityMapper: UserEntityMapper): IEntityMapper<User, UserDomain>
}