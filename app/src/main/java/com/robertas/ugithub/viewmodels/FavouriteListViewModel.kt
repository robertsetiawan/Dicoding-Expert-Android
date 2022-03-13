package com.robertas.ugithub.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertas.ugithub.abstractions.IRepository
import com.robertas.ugithub.models.domain.UserDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteListViewModel @Inject constructor(
    private val userRepository: IRepository<UserDomain>
) : ViewModel() {
    val favouriteList: LiveData<List<UserDomain>> = userRepository.getFavouriteList()

    fun addFavouriteUser(userDomain: UserDomain) {
        viewModelScope.launch {
            userRepository.insertFavouriteUser(userDomain)
        }
    }

    fun getFavouriteUser(userId: Int): LiveData<UserDomain?> {
        return userRepository.getFavouriteUser(userId)
    }

    fun deleteFavouriteUser(userDomain: UserDomain) {
        viewModelScope.launch {
            userRepository.deleteFavouriteUser(userDomain)
        }
    }

    fun deleteFavouriteList() {
        viewModelScope.launch {
            userRepository.deleteFavouriteList()
        }
    }

}