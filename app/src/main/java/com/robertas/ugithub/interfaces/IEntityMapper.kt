package com.robertas.ugithub.interfaces

interface IEntityMapper<T, V> {
    fun maptoEntity(target: V): T
}