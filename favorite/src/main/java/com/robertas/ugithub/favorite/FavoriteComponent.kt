package com.robertas.ugithub.favorite

import android.content.Context
import com.robertas.ugithub.di.FavoriteModuleDependency
import dagger.BindsInstance
import dagger.Component


@Component(dependencies = [FavoriteModuleDependency::class])
interface FavoriteComponent {

    fun inject(fragment: FavoriteListFragment)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(favoriteModuleDependency: FavoriteModuleDependency): Builder
        fun build(): FavoriteComponent
    }
}