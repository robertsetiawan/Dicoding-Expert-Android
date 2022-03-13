package com.robertas.ugithub.abstractions

interface IEntityMapper<T, V> {
    fun maptoEntity(target: V): T

    fun mapFromEntity(source: T): V
}