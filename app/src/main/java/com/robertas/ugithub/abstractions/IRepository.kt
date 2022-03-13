package com.robertas.ugithub.abstractions

import androidx.lifecycle.LiveData


interface IRepository<T> {

    suspend fun getFollowerList(user: String): List<T>

    suspend fun getFollowingList(user: String): List<T>

    suspend fun getDetailUser(user: String): T

    suspend fun getFilteredUser(key: String): List<T>

    suspend fun insertFavouriteUser(user: T)

    suspend fun deleteFavouriteUser(user: T)

    suspend fun deleteFavouriteList()

    fun getFavouriteList(): LiveData<List<T>>

    fun getFavouriteUser(userId: Int): LiveData<T?>
}