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

class FollowerListViewModel: ViewModel() {
    private val userRepository: IRepository<User> = UserRepository(Network.apiService, UserMapper())

    private val _requestGetFollowerList = MutableLiveData<NetworkResult<List<User>>>()

    val requestGetFollowerList: LiveData<NetworkResult<List<User>>> = _requestGetFollowerList

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