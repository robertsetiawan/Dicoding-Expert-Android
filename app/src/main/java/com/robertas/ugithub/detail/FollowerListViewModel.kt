package com.robertas.ugithub.detail

import androidx.lifecycle.ViewModel
import com.robertas.ugithub.core.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FollowerListViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {
    fun getFollowerList(username: String) = userUseCase.getFollowerList(username)
}