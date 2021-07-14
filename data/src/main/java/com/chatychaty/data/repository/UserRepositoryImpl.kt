package com.chatychaty.data.repository

import com.chatychaty.data.asBearerToken
import com.chatychaty.data.request
import com.chatychaty.domain.model.Theme
import com.chatychaty.domain.model.User
import com.chatychaty.domain.repository.UserRepository
import com.chatychaty.local.UserDao
import com.chatychaty.remote.user.RemoteUser
import com.chatychaty.remote.user.RemoteUserDataSource
import com.chatychaty.remote.user.UpdatePasswordBody
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

private const val IMAGE_NAME = "photoFile"

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val remoteUserDataSource: RemoteUserDataSource,
    override val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UserRepository {

    private val token: Flow<String> = userDao.getUserToken()

    override suspend fun signUp(user: User): Result<User> = request(dispatcher) {
        remoteUserDataSource.signUp(user.toRemoteUser())
    }.onSuccess {
        withContext(dispatcher) {
            userDao.createOrUpdateUser(it.user.toDomainUser(), it.token)
        }
    }.map { it.user.toDomainUser() }

    override suspend fun signIn(user: User): Result<User> = request(dispatcher) {
        remoteUserDataSource.signIn(user.toRemoteUser())
    }.onSuccess {
        withContext(dispatcher) {
            userDao.createOrUpdateUser(it.user.toDomainUser(), it.token)
        }
    }.map { it.user.toDomainUser() }

    override suspend fun updateImage(byteArray: ByteArray, fileName: String): Result<Unit> = request(dispatcher) {
        val requestBody = byteArray.toRequestBody(MultipartBody.FORM)
        val multipartBody = MultipartBody.Part.createFormData(IMAGE_NAME, fileName, requestBody)

        remoteUserDataSource.updateImage(token.asBearerToken(), multipartBody)
    }.onSuccess { newUrl ->
        withContext(dispatcher) {
            userDao.getUser()
                .single()
                ?.copy(imageUrl = newUrl)
                ?.also { userDao.createOrUpdateUser(it, null) }
        }
    }.map { }

    override suspend fun updateName(name: String): Result<Unit> = request(dispatcher) {
        remoteUserDataSource.updateName(token.asBearerToken(), name)
    }.onSuccess { newName ->
        withContext(dispatcher) {
            userDao.getUser()
                .first()
                ?.copy(name = newName)
                ?.also { userDao.createOrUpdateUser(it, null) }
        }
    }.map { }

    override suspend fun updatePassword(currentPassword: String, newPassword: String): Result<Unit> = request(dispatcher) {
        val updatePasswordBody = UpdatePasswordBody(currentPassword, newPassword)
        remoteUserDataSource.updatePassword(token.asBearerToken(), updatePasswordBody)
    }

    override suspend fun signOut() = withContext(dispatcher) { userDao.deleteUser() }

    override fun getCurrentUser(): Flow<User?> = userDao.getUser()

    override fun getTheme(): Flow<Theme> = userDao.getTheme()

    override suspend fun updateTheme(value: Theme) = withContext(dispatcher) {
        userDao.updateTheme(value)
    }

    override suspend fun submitFeedback(stars: Int, feedback: String): Result<Unit> = request(dispatcher) {
        remoteUserDataSource.submitFeedback(token.asBearerToken(), stars, feedback)
    }

    override suspend fun deleteAccount(): Result<Unit> = request(dispatcher) {
        remoteUserDataSource.deleteAccount(token.asBearerToken())
    }

    private fun RemoteUser.toDomainUser(): User {
        return User(
            name = displayName,
            username = username,
            password = null,
            imageUrl = photoURL,
        )
    }

    private fun User.toRemoteUser(): RemoteUser {
        return RemoteUser(
            name,
            username,
            password,
            imageUrl,
        )
    }
}
