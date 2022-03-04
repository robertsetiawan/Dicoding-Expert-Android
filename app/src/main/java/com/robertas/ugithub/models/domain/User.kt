package com.robertas.ugithub.models.domain

data class User(
	val login: String = "",
	val bio: String = "",
	val company: String = "",
	val id: Int = 0,
	val publicRepos: Int = 0,
	val pubicGist: Int = 0,
	val email: String = "",
	val followers: Int = 0,
	val avatarUrl: String = "",
	val htmlUrl: String = "",
	val following: Int = 0,
	val name: String = "",
	val location: String = "",
)

