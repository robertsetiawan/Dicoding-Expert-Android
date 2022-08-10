package com.robertas.ugithub.core.domain.repository

import com.robertas.ugithub.core.data.NetworkResult
import com.robertas.ugithub.core.domain.model.UserDomain
import kotlinx.coroutines.flow.Flow


interface IUserRepository {
    fun getFollowerList(user: String): Flow<NetworkResult<List<UserDomain>>>
    fun getFollowingList(user: String): Flow<NetworkResult<List<UserDomain>>>
    fun getDetailUser(user: String): Flow<NetworkResult<UserDomain>>
    fun getFilteredUser(key: String): Flow<NetworkResult<List<UserDomain>>>
    suspend fun insertFavouriteUser(user: UserDomain)
    suspend fun deleteFavouriteUser(user: UserDomain)
    suspend fun deleteFavouriteList()
    fun getFavouriteList(): Flow<List<UserDomain>>
    fun getFavouriteUser(userId: Int): Flow<UserDomain?>
}