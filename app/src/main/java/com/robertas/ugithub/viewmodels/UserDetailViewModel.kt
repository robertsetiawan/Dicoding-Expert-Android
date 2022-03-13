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
class UserDetailViewModel @Inject constructor(
    private val userRepository: IRepository<UserDomain>
) : ViewModel() {
    private val _requestGetDetailUser = MutableLiveData<NetworkResult<UserDomain>>()

    val requestGetDetailUser: LiveData<NetworkResult<UserDomain>> = _requestGetDetailUser

    fun doneNavigatingToDetailFragment() {
        _requestGetDetailUser.value = NetworkResult.Loading()
    }

    fun getDetailUser(username: String) {
        _requestGetDetailUser.value = NetworkResult.Loading()

        viewModelScope.launch {
            try {
                val detailUser = userRepository.getDetailUser(username)

                _requestGetDetailUser.value = NetworkResult.Loaded(detailUser)
            } catch (e: Exception) {
                _requestGetDetailUser.value = NetworkResult.Error(UserDomain(), e.message)
            }
        }
    }
}