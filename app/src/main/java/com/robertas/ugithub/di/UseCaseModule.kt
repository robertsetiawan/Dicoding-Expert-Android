package com.robertas.ugithub.di

import com.robertas.ugithub.core.domain.usecase.UserInteractor
import com.robertas.ugithub.core.domain.usecase.UserUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindUserInteractor(userInteractor: UserInteractor): UserUseCase
}