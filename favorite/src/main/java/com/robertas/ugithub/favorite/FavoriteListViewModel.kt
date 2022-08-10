package com.robertas.ugithub.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.robertas.ugithub.core.domain.model.UserDomain
import com.robertas.ugithub.core.domain.usecase.UserUseCase


class FavoriteListViewModel (
    private val userUseCase: UserUseCase
) : ViewModel() {
    val favouriteList: LiveData<List<UserDomain>> = userUseCase.getFavouriteList().asLiveData()

    suspend fun deleteFavouriteList() = userUseCase.deleteFavouriteList()
}