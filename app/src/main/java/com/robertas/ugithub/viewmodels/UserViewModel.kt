package com.robertas.ugithub.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertas.ugithub.abstractions.IRepository
import com.robertas.ugithub.models.domain.UserDomain
import com.robertas.ugithub.models.network.enums.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: IRepository<UserDomain>
) : ViewModel() {

    private val _requestGetUserList = MutableLiveData<NetworkResult<List<UserDomain>>>()

    val requestGetUserList: LiveData<NetworkResult<List<UserDomain>>> = _requestGetUserList

    fun getFilteredUserList(key: String) {
        _requestGetUserList.value = NetworkResult.Loading()

        viewModelScope.launch {

            try {
                val items = userRepository.getFilteredUser(key)

                _requestGetUserList.value = NetworkResult.Loaded(items)

            } catch (e: Exception) {
                _requestGetUserList.value = NetworkResult.Error(arrayListOf(), e.message)
            }
        }
    }

    init {
        getFilteredUserList(DEFAULT_KEYWORD)
    }

    companion object {
        const val DEFAULT_KEYWORD = "agung"
    }
}