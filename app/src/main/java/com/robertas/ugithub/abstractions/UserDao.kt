package com.robertas.ugithub.abstractions

import androidx.room.*
import com.robertas.ugithub.models.entity.User
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