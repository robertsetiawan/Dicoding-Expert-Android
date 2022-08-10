package com.robertas.ugithub.core.data

sealed class NetworkResult<out R> {
    data class Success<out T>(val data: T) : NetworkResult<T>()

    data class Error(val message: String) : NetworkResult<Nothing>()

    object Loading : NetworkResult<Nothing>()
}