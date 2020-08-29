package com.chatychaty.data.repository

import com.chatychaty.data.retrieveErrors
import com.chatychaty.data.source.local.UserLocalDataSource
import com.chatychaty.data.source.remote.UserRemoteDataSource
import com.chatychaty.data.tryCatching
import com.chatychaty.domain.model.User
import com.chatychaty.domain.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserRepositoryImpl(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {

    override val token: String
        get() = AUTH_SCHEME.plus(userLocalDataSource.getUserToken())

    override suspend fun signUp(user: User): Result<User> = withContext(Dispatchers.IO) {

        tryCatching {
            userRemoteDataSource.signUp(user)
        }.mapCatching { response ->

            if (response.success) {
                userLocalDataSource.createUser(response.data!!.user, response.data.token)
                Result.success(response.data.user)
            } else {
                Result.failure(Throwable(response.retrieveErrors()))
            }

        }.getOrElse { throwable ->
            Result.failure(throwable)
        }

    }


    override suspend fun signIn(user: User): Result<User> = withContext(Dispatchers.IO) {

        tryCatching {
            userRemoteDataSource.signIn(user)
        }.mapCatching { response ->

            if (response.success) {
                userLocalDataSource.createUser(response.data!!.user, response.data.token)
                Result.success(response.data.user)
            } else {
                Result.failure(Throwable(response.retrieveErrors()))
            }

        }.getOrElse { throwable ->
            Result.failure(throwable)
        }

    }

    override suspend fun updateImage(byteArray: ByteArray, fileName: String): Result<Unit> = withContext(Dispatchers.IO) {

        val requestBody = byteArray.toRequestBody(MultipartBody.FORM)
        val multipartBody = MultipartBody.Part.createFormData(IMAGE_NAME, fileName, requestBody)

        tryCatching {
            userRemoteDataSource.updateImage(token, multipartBody)
        }.mapCatching { response ->

            if (response.success) {
                userLocalDataSource.setUserValue(IMAGE_URL, response.data!!)
                Result.success(Unit)
            } else {
                Result.failure(Throwable(response.retrieveErrors()))
            }

        }.getOrElse { throwable ->
            Result.failure(throwable)
        }

    }

    override suspend fun updateName(name: String): Result<Unit> = withContext(Dispatchers.IO) {

        tryCatching {
            userRemoteDataSource.updateName(token, name)
        }.mapCatching { response ->

            if (response.success) {
                userLocalDataSource.setUserValue(NAME, name)
                Result.success(Unit)
            } else {
                Result.failure(Throwable(response.retrieveErrors()))
            }

        }.getOrElse { throwable ->
            Result.failure(throwable)
        }

    }

    override suspend fun signOut() = withContext(Dispatchers.IO) { userLocalDataSource.deleteUser() }

    override suspend fun getUser(): Result<Flow<User>> {
        return Result.success(userLocalDataSource.getUser())
    }

    override suspend fun getTheme(): String {
        return userLocalDataSource.getThemeValue()
    }

    override suspend fun setTheme(value: String) {
        userLocalDataSource.setThemeValue(value)
    }

    override suspend fun submitFeedback(stars: Int, feedback: String): Result<Unit> = withContext(Dispatchers.IO) {

        tryCatching {
            userRemoteDataSource.submitFeedback(token, stars, feedback)
        }.mapCatching { response ->

            if (response.success) Result.success(Unit) else Result.failure(Throwable(response.retrieveErrors()))

        }.getOrElse { throwable ->
            Result.failure(throwable)
        }

    }

    override suspend fun deleteAccount(): Result<Unit> = withContext(Dispatchers.IO) {

        tryCatching {
            userRemoteDataSource.deleteAccount(token)
        }.mapCatching { response ->

            if (response.success) Result.success(Unit) else Result.failure(Throwable(response.retrieveErrors()))

        }.getOrElse { throwable ->
            Result.failure(throwable)
        }

    }

}