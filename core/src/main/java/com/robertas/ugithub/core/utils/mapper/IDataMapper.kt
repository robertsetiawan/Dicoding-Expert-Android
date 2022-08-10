package com.robertas.ugithub.core.utils.mapper

interface IDataMapper<T, V> {
    fun mapFromData(source: T): V
}