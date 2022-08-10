package com.robertas.ugithub.di

import com.robertas.ugithub.core.domain.usecase.UserUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FavoriteModuleDependency {

    fun provideUserUseCase(): UserUseCase
}