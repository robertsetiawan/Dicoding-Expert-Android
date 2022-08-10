package com.robertas.ugithub.core.data.source.remote.network

import com.robertas.ugithub.core.data.source.remote.response.SearchResponse
import com.robertas.ugithub.core.data.source.remote.response.UserNetwork
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IUserGithubService {

    @GET("users/{user}")
    suspend fun getDetailUserAsync(
        @Path("user") username: String
    ): Response<UserNetwork>

    @GET("search/users")
    suspend fun getFilteredUserAsync(
        @Query("q") key: String
    ): Response<SearchResponse>

    @GET("users/{user}/followers")
    suspend fun getFollowerListAsync(
        @Path("user") username: String
    ): Response<List<UserNetwork>>

    @GET("users/{user}/following")
    suspend fun getFollowingListAsync(
        @Path("user") username: String
    ): Response<List<UserNetwork>>

}