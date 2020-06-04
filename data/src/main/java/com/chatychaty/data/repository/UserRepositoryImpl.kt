package com.chatychaty.data.repository

import com.chatychaty.data.source.local.UserLocalDataSource
import com.chatychaty.data.source.remote.UserRemoteDataSource
import com.chatychaty.data.util.tryCatching
import com.chatychaty.domain.model.User
import com.chatychaty.domain.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository {

    override val token: String
        get() = AUTH_SCHEME.plus(userLocalDataSource.getUserToken())

    override suspend fun signUp(user: User): Result<User> {

        return withContext(Dispatchers.IO) {
            tryCatching {
                userRemoteDataSource.signUp(user)
            }.mapCatching { response ->
                if (response.condition) {
                    userLocalDataSource.createUser(response.user!!, response.token!!)
                    Result.success(response.user!!)
                } else {
                    Result.failure(Throwable(response.errors.toString()))
                }
            }.getOrElse {
                Result.failure(it)
            }
        }
    }


    override suspend fun signIn(user: User): Result<User> {
        return withContext(Dispatchers.IO) {
            tryCatching {
                userRemoteDataSource.signIn(user)
            }.mapCatching { response ->
                if (response.condition) {
                    userLocalDataSource.createUser(response.user!!, response.token!!)
                    Result.success(response.user!!)
                } else {
                    Result.failure(Throwable(response.errors.toString()))
                }
            }.getOrElse {
                Result.failure(it)
            }
        }
    }

    override suspend fun updatePhoto(byteArray: ByteArray, fileName: String) {
        withContext(Dispatchers.IO) {


            val body = byteArray.toRequestBody(MultipartBody.FORM)
            val mp = MultipartBody.Part.createFormData(IMG_NAME, fileName, body)

            tryCatching {
                userRemoteDataSource.updatePhoto(token, mp).also {
                    userLocalDataSource.setUserValue(IMG_URL, it)
                }
            }

            userRemoteDataSource.updatePhoto(token, mp).also {
                userLocalDataSource.setUserValue(IMG_URL, it)
            }
        }
    }

    override suspend fun updateName(name: String) {
        withContext(Dispatchers.IO) {
            userRemoteDataSource.updateName(token, name)
            userLocalDataSource.setUserValue(NAME, name)
        }
    }

    override suspend fun signOut() {
        withContext(Dispatchers.IO) {
            userLocalDataSource.deleteUser()
        }
    }

    override suspend fun getUser(): Result<Flow<User>> {
        return Result.success(userLocalDataSource.getUser())
    }

    override suspend fun getThemeValue(): String {
        return userLocalDataSource.getThemeValue()
    }

    override suspend fun setThemeValue(value: String) {
        userLocalDataSource.setThemeValue(value)
    }

}