package com.robertas.ugithub.core.data.source

import com.robertas.ugithub.core.data.NetworkResult
import com.robertas.ugithub.core.data.source.local.LocalDataSource
import com.robertas.ugithub.core.data.source.local.entity.User
import com.robertas.ugithub.core.data.source.remote.RemoteDataSource
import com.robertas.ugithub.core.data.source.remote.response.UserNetwork
import com.robertas.ugithub.core.domain.model.UserDomain
import com.robertas.ugithub.core.domain.repository.IUserRepository
import com.robertas.ugithub.core.utils.mapper.IDataMapper
import com.robertas.ugithub.core.utils.mapper.IMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val userNetworkMapper: IDataMapper<UserNetwork, UserDomain>,
    private val userEntityMapper: IMapper<User, UserDomain>
) : IUserRepository {
    override fun getFollowerList(user: String): Flow<NetworkResult<List<UserDomain>>> {
        return remoteDataSource.getFollowerList(user).map { value ->
            when (value) {
                is NetworkResult.Loading -> NetworkResult.Loading
                is NetworkResult.Success -> NetworkResult.Success(value.data.map { element ->
                    userNetworkMapper.mapFromData(
                        element
                    )
                })
                is NetworkResult.Error -> NetworkResult.Error(value.message)
            }
        }.flowOn(Dispatchers.Default)
    }

    override fun getFollowingList(user: String): Flow<NetworkResult<List<UserDomain>>> {
        return remoteDataSource.getFollowingList(user).map { value ->
            when (value) {
                is NetworkResult.Loading -> NetworkResult.Loading
                is NetworkResult.Success -> NetworkResult.Success(value.data.map { element ->
                    userNetworkMapper.mapFromData(
                        element
                    )
                })
                is NetworkResult.Error -> NetworkResult.Error(value.message)
            }
        }.flowOn(Dispatchers.Default)
    }

    override fun getFilteredUser(key: String): Flow<NetworkResult<List<UserDomain>>> {
        return remoteDataSource.getFilteredUser(key).map { value ->
            when (value) {
                is NetworkResult.Loading -> NetworkResult.Loading
                is NetworkResult.Success -> NetworkResult.Success(value.data.map { element ->
                    userNetworkMapper.mapFromData(
                        element
                    )
                })
                is NetworkResult.Error -> NetworkResult.Error(value.message)
            }
        }.flowOn(Dispatchers.Default)
    }

    override fun getDetailUser(user: String): Flow<NetworkResult<UserDomain>> {
        return remoteDataSource.getDetailUser(user).map { value ->
            when (value) {
                is NetworkResult.Loading -> NetworkResult.Loading
                is NetworkResult.Success -> {
                    if (value.data == null) {
                        NetworkResult.Error("User not found")
                    } else {
                        NetworkResult.Success(userNetworkMapper.mapFromData(value.data))
                    }
                }
                is NetworkResult.Error -> NetworkResult.Error(value.message)
            }
        }.flowOn(Dispatchers.Default)
    }

    override suspend fun insertFavouriteUser(user: UserDomain) {
        val entity = userEntityMapper.mapFromDomain(user)
        localDataSource.insertUser(entity)
    }

    override suspend fun deleteFavouriteUser(user: UserDomain) {
        val entity = userEntityMapper.mapFromDomain(user)
        localDataSource.deleteUser(entity)
    }

    override suspend fun deleteFavouriteList() {
        localDataSource.deleteAllUser()
    }

    override fun getFavouriteList(): Flow<List<UserDomain>> {
        return localDataSource.getUserList().map { value ->
            value.map { userEntityMapper.mapFromData(it) }
        }
    }

    override fun getFavouriteUser(userId: Int): Flow<UserDomain?> {
        return localDataSource.getUser(userId).map { value ->
            if (value == null) {
                null
            } else {
                userEntityMapper.mapFromData(value)
            }
        }
    }
}