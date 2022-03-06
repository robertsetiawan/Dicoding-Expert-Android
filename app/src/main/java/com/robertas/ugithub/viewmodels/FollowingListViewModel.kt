package com.robertas.ugithub.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertas.ugithub.interfaces.IRepository
import com.robertas.ugithub.models.domain.User
import com.robertas.ugithub.models.network.enums.NetworkResult
import com.robertas.ugithub.repositories.UserRepository
import com.robertas.ugithub.services.Network
import com.robertas.ugithub.utils.mappers.UserMapper
import kotlinx.coroutines.launch

class FollowingListViewModel : ViewModel() {
    private val userRepository: IRepository<User> = UserRepository(Network.apiService, UserMapper())

    private val _requestGetFollowingList = MutableLiveData<NetworkResult<List<User>>>()

    val requestGetFollowingList: LiveData<NetworkResult<List<User>>> = _requestGetFollowingList

    fun getFollowingList(username: String) {
        _requestGetFollowingList.value = NetworkResult.Loading()

        viewModelScope.launch {
            try {
                val followingList = userRepository.getFollowingList(username)

                _requestGetFollowingList.value = NetworkResult.Loaded(followingList)

            } catch (e: Exception) {
                _requestGetFollowingList.value = NetworkResult.Error(arrayListOf(), e.message)
            }
        }
    }
}