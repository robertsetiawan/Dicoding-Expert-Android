package com.robertas.ugithub.core.data.source.local.room

import androidx.room.*
import com.robertas.ugithub.core.data.source.local.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM user")
    fun getFavouriteList(): Flow<List<User>>

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM user WHERE id = :userId")
    fun getFavouriteUser(userId: Int): Flow<User?>

    @Query("DELETE FROM user")
    suspend fun deleteFavouriteList()
}