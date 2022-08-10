package com.robertas.ugithub.core.utils.mapper

interface IDomainMapper<T, V> {
    fun mapFromDomain(source: T): V
}