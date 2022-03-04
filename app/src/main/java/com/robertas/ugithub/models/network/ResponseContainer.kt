package com.robertas.ugithub.models.network

abstract class ResponseContainer<T> {
    abstract val totalCount: Int

    abstract val incompleteResults: Boolean

    abstract val items: List<T>
}