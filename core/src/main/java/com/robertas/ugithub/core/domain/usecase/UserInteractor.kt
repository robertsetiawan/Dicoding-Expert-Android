package com.robertas.ugithub.core.domain.usecase

import com.robertas.ugithub.core.domain.model.UserDomain
import com.robertas.ugithub.core.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserInteractor @Inject constructor(private val userRepository: IUserRepository) : UserUseCase {
    override fun getFollowerList(user: String): Flow<com.robertas.ugithub.core.data.NetworkResult<List<UserDomain>>> =
        userRepository.getFollowerList(user)

    override fun getFollowingList(user: String): Flow<com.robertas.ugithub.core.data.NetworkResult<List<UserDomain>>> =
        userRepository.getFollowingList(user)

    override fun getDetailUser(user: String): Flow<com.robertas.ugithub.core.data.NetworkResult<UserDomain>> =
        userRepository.getDetailUser(user)

    override fun getFilteredUser(key: String): Flow<com.robertas.ugithub.core.data.NetworkResult<List<UserDomain>>> =
        userRepository.getFilteredUser(key)

    override suspend fun insertFavouriteUser(user: UserDomain) =
        userRepository.insertFavouriteUser(user)

    override suspend fun deleteFavouriteUser(user: UserDomain) =
        userRepository.deleteFavouriteUser(user)

    override suspend fun deleteFavouriteList() = userRepository.deleteFavouriteList()

    override fun getFavouriteList(): Flow<List<UserDomain>> = userRepository.getFavouriteList()

    override fun getFavouriteUser(userId: Int): Flow<UserDomain?> =
        userRepository.getFavouriteUser(userId)
}