package com.robertas.ugithub.abstractions

interface IEntityMapper<T, V> {
    fun mapToEntity(target: V): T

    fun mapFromEntity(source: T): V
}