package com.robertas.ugithub.interfaces

interface IRepository<T> {

    suspend fun getFollowerList(user: String): List<T>

    suspend fun getFollowingList(user: String): List<T>

    suspend fun getDetailUser(user: String): T

    suspend fun getFilteredUser(key: String): List<T>

}