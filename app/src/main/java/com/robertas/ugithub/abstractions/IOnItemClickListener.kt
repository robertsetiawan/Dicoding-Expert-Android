package com.robertas.ugithub.abstractions


interface IOnItemClickListener<T> {
    fun onClick(obj: T)
}