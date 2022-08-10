package com.robertas.ugithub.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.robertas.ugithub.core.data.NetworkResult
import com.robertas.ugithub.core.domain.model.UserDomain
import com.robertas.ugithub.core.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {
    var firstLoad = MediatorLiveData<NetworkResult<List<UserDomain>>>()

    private fun getFilteredUserList(key: String) = userUseCase.getFilteredUser(key).asLiveData()

    fun updateFilteredUserList(key: String) {
        firstLoad.addSource(getFilteredUserList(key)){
            firstLoad.value = it
        }
    }

    companion object {
        const val DEFAULT_KEYWORD = "agung"
    }

    init {
        updateFilteredUserList(DEFAULT_KEYWORD)
    }
}