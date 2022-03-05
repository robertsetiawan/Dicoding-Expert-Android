package com.robertas.ugithub.models.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
	val login: String = "",
	val bio: String = "",
	val company: String = "",
	val id: Int = 0,
	val publicRepos: Int = 0,
	val followers: Int = 0,
	val avatarUrl: String = "",
	val blog: String = "",
	val following: Int = 0,
	val name: String = "",
	val location: String = "",
): Parcelable

