package com.robertas.ugithub.interfaces

import android.content.Context

interface IRepository<T> {
    fun getListItemFromResources(context: Context): List<T>
}