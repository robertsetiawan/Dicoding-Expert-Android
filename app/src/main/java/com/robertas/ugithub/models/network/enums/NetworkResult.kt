package com.robertas.ugithub.models.network.enums

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loaded<T>(data: T) : NetworkResult<T>(data)

    class Error<T>(data: T?, message: String?) : NetworkResult<T>(data, message)

    class Loading<T> : NetworkResult<T>()
}