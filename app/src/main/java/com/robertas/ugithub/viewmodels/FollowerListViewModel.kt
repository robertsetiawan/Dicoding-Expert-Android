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
class FollowerListViewModel @Inject constructor(
    private val userRepository: IRepository<UserDomain>
) : ViewModel() {

    private val _requestGetFollowerList = MutableLiveData<NetworkResult<List<UserDomain>>>()

    val requestGetFollowerList: LiveData<NetworkResult<List<UserDomain>>> = _requestGetFollowerList

    fun getFollowerList(username: String) {
        _requestGetFollowerList.value = NetworkResult.Loading()

        viewModelScope.launch {
            try {
                val followerList = userRepository.getFollowerList(username)

                _requestGetFollowerList.value = NetworkResult.Loaded(followerList)
            } catch (e: Exception) {
                _requestGetFollowerList.value = NetworkResult.Error(arrayListOf(), e.message)
            }
        }
    }
}