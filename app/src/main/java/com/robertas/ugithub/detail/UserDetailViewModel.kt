package com.robertas.ugithub.detail

import androidx.lifecycle.ViewModel
import com.robertas.ugithub.core.domain.model.UserDomain
import com.robertas.ugithub.core.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {
    fun getDetailUser(username: String) = userUseCase.getDetailUser(username)

    suspend fun addFavouriteUser(userDomain: UserDomain) {
        userUseCase.insertFavouriteUser(userDomain)
    }

    fun getFavouriteUser(userId: Int) = userUseCase.getFavouriteUser(userId)

    suspend fun deleteFavouriteUser(userDomain: UserDomain) =
        userUseCase.deleteFavouriteUser(userDomain)
}