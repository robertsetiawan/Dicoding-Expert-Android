package com.robertas.ugithub.core.data.source.local

import com.robertas.ugithub.core.data.source.local.entity.User
import com.robertas.ugithub.core.data.source.local.room.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val userDao: UserDao) {
    suspend  fun insertUser(entity: User) {
        withContext(Dispatchers.IO) { userDao.insert(entity) }
    }

    suspend fun deleteUser(entity: User) {
        withContext(Dispatchers.IO) { userDao.delete(entity) }
    }

    suspend fun deleteAllUser() {
        withContext(Dispatchers.IO) { userDao.deleteFavouriteList() }
    }

    fun getUserList(): Flow<List<User>> = userDao.getFavouriteList()

    fun getUser(userId: Int): Flow<User?> = userDao.getFavouriteUser(userId)
}