package com.robertas.ugithub.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val username: String,
    val name: String,
    @DrawableRes
    val avatar: Int,
    val company: String,
    val location: String,
    val repository: Int,
    val followers: Int,
    val following: Int
): Parcelable
