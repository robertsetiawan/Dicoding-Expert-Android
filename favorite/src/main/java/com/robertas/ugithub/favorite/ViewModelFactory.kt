package com.robertas.ugithub.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.robertas.ugithub.core.domain.usecase.UserUseCase
import javax.inject.Inject

class ViewModelFactory @Inject constructor(private val userUseCase: UserUseCase) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(FavoriteListViewModel::class.java) -> {
                FavoriteListViewModel(userUseCase) as T
            }
            else -> throw Throwable("Unknown ViewModel class: ${modelClass.name}")
        }
}