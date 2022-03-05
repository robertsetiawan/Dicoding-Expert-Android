package com.robertas.ugithub.services

import com.robertas.ugithub.interfaces.IUserGithubService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object Network {
    private const val API_KEY = "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"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request: Request =
                chain.request().newBuilder().addHeader("Authorization", "token $API_KEY").build()
            chain.proceed(request)
        }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client.build())
        .build()

    val apiService: IUserGithubService = retrofit.create(IUserGithubService::class.java)
}