package com.robertas.ugithub.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.robertas.ugithub.core.data.source.local.entity.User


@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "user_db"
    }
}