package com.robertas.ugithub.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User constructor(
    @PrimaryKey
    val id: Int,
    val avatarUrl: String,
    val login: String
)