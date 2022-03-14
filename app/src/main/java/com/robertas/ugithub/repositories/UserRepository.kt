package com.robertas.ugithub.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import com.robertas.ugithub.abstractions.IEntityMapper
import com.robertas.ugithub.abstractions.IRepository
import com.robertas.ugithub.abstractions.IUserGithubService
import com.robertas.ugithub.abstractions.UserDao
import com.robertas.ugithub.models.domain.UserDomain
import com.robertas.ugithub.models.entity.User
import com.robertas.ugithub.models.network.SearchResponse
import com.robertas.ugithub.models.network.UserNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: IUserGithubService,

    private val userDomainMapper: IEntityMapper<UserDomain, UserNetwork>,

    private val userEntityMapper: IEntityMapper<User, UserDomain>,

    private val userDao: UserDao

) : IRepository<UserDomain> {

    override suspend fun getFollowerList(user: String): List<UserDomain> {
        val response: Response<List<UserNetwork>>

        withContext(Dispatchers.IO) {
            response = apiService.getFollowerListAsync(user)
        }

        when (response.code()) {
            200 -> return response.body()?.map { element -> userDomainMapper.mapToEntity(element) }
                .orEmpty()
                .toList()

            else -> throw Exception(getMessageFromApi(response))
        }
    }

    override suspend fun getFollowingList(user: String): List<UserDomain> {
        val response: Response<List<UserNetwork>>

        withContext(Dispatchers.IO) {
            response = apiService.getFollowingListAsync(user)
        }

        when (response.code()) {
            200 -> return response.body()?.map { element -> userDomainMapper.mapToEntity(element) }
                .orEmpty()
                .toList()

            else -> throw Exception(getMessageFromApi(response))
        }
    }

    override suspend fun getDetailUser(user: String): UserDomain {
        val response: Response<UserNetwork>

        withContext(Dispatchers.IO) {
            response = apiService.getDetailUserAsync(user)
        }

        when (response.code()) {
            200 -> return response.body()?.let { userDomainMapper.mapToEntity(it) } ?: UserDomain()

            else -> throw Exception(getMessageFromApi(response))
        }
    }

    override suspend fun getFilteredUser(key: String): List<UserDomain> {
        val response: Response<SearchResponse>

        withContext(Dispatchers.IO) {
            response = apiService.getFilteredUserAsync(key)
        }

        when (response.code()) {
            200 -> return response.body()?.items?.map { element ->
                userDomainMapper.mapToEntity(
                    element
                )
            }.orEmpty()
                .toList()

            else -> throw Exception(getMessageFromApi(response))
        }
    }

    override suspend fun insertFavouriteUser(user: UserDomain) {
        val userEntity = userEntityMapper.mapToEntity(user)

        withContext(Dispatchers.IO) { userDao.insert(userEntity) }
    }

    override fun getFavouriteList(): LiveData<List<UserDomain>> =
        Transformations.map(userDao.getFavouriteList().asLiveData()) { list ->
            list.map { userEntityMapper.mapFromEntity(it) }
        }

    override suspend fun deleteFavouriteUser(user: UserDomain) {
        val userEntity = userEntityMapper.mapToEntity(user)

        withContext(Dispatchers.IO) { userDao.delete(userEntity) }
    }

    override fun getFavouriteUser(userId: Int): LiveData<UserDomain?> =
        Transformations.map(userDao.getFavouriteUser(userId).asLiveData()) {
            it?.let { userEntityMapper.mapFromEntity(it) }
        }

    override suspend fun deleteFavouriteList() {
        withContext(Dispatchers.IO) { userDao.deleteFavouriteList() }
    }

    private fun getMessageFromApi(response: Response<*>): String {
        val jsonObj = JSONObject(response.errorBody()?.charStream()?.readText().orEmpty())

        return jsonObj.getString("message").orEmpty()
    }
}