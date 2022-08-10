package com.robertas.ugithub.core.data.source.remote

import com.robertas.ugithub.core.data.NetworkResult
import com.robertas.ugithub.core.data.source.remote.network.IUserGithubService
import com.robertas.ugithub.core.data.source.remote.response.UserNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val userService: IUserGithubService
) {

    fun getFollowerList(user: String): Flow<NetworkResult<List<UserNetwork>>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = userService.getFollowerListAsync(user)
            when (response.code()) {
                200 -> emit(NetworkResult.Success(response.body().orEmpty()))

                else -> emit(NetworkResult.Error(getMessageFromApi(response)))
            }

        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun getFollowingList(user: String): Flow<NetworkResult<List<UserNetwork>>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = userService.getFollowingListAsync(user)
            when (response.code()) {
                200 -> emit(NetworkResult.Success(response.body().orEmpty()))

                else -> emit(NetworkResult.Error(getMessageFromApi(response)))
            }

        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun getDetailUser(user: String): Flow<NetworkResult<UserNetwork?>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = userService.getDetailUserAsync(user)
            when (response.code()) {
                200 -> emit(NetworkResult.Success(response.body()))

                else -> emit(NetworkResult.Error(getMessageFromApi(response)))
            }

        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun getFilteredUser(key: String): Flow<NetworkResult<List<UserNetwork>>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = userService.getFilteredUserAsync(key)
            when (response.code()) {
                200 -> emit(NetworkResult.Success(response.body()?.items.orEmpty()))

                else -> emit(NetworkResult.Error(getMessageFromApi(response)))
            }

        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    private fun getMessageFromApi(response: Response<*>): String {
        val jsonObj = JSONObject(response.errorBody()?.charStream()?.readText().orEmpty())
        return jsonObj.getString("message").orEmpty()
    }
}