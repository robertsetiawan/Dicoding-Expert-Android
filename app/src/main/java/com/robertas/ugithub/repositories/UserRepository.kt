package com.robertas.ugithub.repositories

import com.robertas.ugithub.interfaces.IEntityMapper
import com.robertas.ugithub.interfaces.IRepository
import com.robertas.ugithub.interfaces.IUserGithubService
import com.robertas.ugithub.models.domain.User
import com.robertas.ugithub.models.network.SearchResponse
import com.robertas.ugithub.models.network.UserNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response

class UserRepository(
    private val apiService: IUserGithubService,

    private val userMapper: IEntityMapper<User, UserNetwork>
) : IRepository<User> {

    override suspend fun getFollowerList(user: String): List<User> {
        val response: Response<List<UserNetwork>>

        withContext(Dispatchers.IO) {
            response = apiService.getFollowerListAsync(user)
        }

        when (response.code()) {
            200 -> return response.body()?.map { element -> userMapper.maptoEntity(element) }.orEmpty()
                .toList()

            else -> throw Exception(getMessageFromApi(response))
        }
    }

    override suspend fun getFollowingList(user: String): List<User> {
        val response: Response<List<UserNetwork>>

        withContext(Dispatchers.IO) {
            response = apiService.getFollowingListAsync(user)
        }

        when (response.code()) {
            200 -> return response.body()?.map { element -> userMapper.maptoEntity(element) }.orEmpty()
                .toList()

            else -> throw Exception(getMessageFromApi(response))
        }
    }

    override suspend fun getDetailUser(user: String): User {
        val response: Response<UserNetwork>

        withContext(Dispatchers.IO) {
            response = apiService.getDetailUserAsync(user)
        }

        when (response.code()) {
            200 -> return response.body()?.let { userMapper.maptoEntity(it) } ?: User()

            else -> throw Exception(getMessageFromApi(response))
        }
    }

    override suspend fun getFilteredUser(key: String): List<User> {
        val response: Response<SearchResponse>

        withContext(Dispatchers.IO) {
            response = apiService.getFilteredUserAsync(key)
        }

        when (response.code()) {
            200 -> return response.body()?.items?.map { element -> userMapper.maptoEntity(element) }.orEmpty()
                .toList()

            else -> throw Exception(getMessageFromApi(response))
        }
    }

    private fun getMessageFromApi(response: Response<*>): String {
        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())

        return jsonObj.getString("message")
    }
}