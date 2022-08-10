package com.robertas.ugithub.core.utils.mapper

interface IMapper<T,V>: IDataMapper<T, V>, IDomainMapper<V, T>