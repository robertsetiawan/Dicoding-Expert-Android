package com.robertas.ugithub.detail

import androidx.lifecycle.ViewModel
import com.robertas.ugithub.core.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FollowingListViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {
    fun getFollowingList(username: String) = userUseCase.getFollowingList(username)
}