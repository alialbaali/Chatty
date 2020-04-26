package com.apps.chatychaty.repo

import android.content.SharedPreferences
import androidx.core.content.edit
import com.apps.chatychaty.di.AUTH_SCHEME
import com.apps.chatychaty.model.User
import com.apps.chatychaty.remote.UserClient
import com.apps.chatychaty.token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import timber.log.Timber

const val TOKEN = "token"
const val NAME = "name"
const val USERNAME = "username"
const val IMG_URL = "img_url"

class UserRepository(private val userClient: UserClient, private val sharedPreferences: SharedPreferences) {

    suspend fun signUp(user: User): List<String>? {
        return withContext(Dispatchers.IO) {
            val response = userClient.signUp(user)
            if (response.condition) {
                insertUser(response.user!!, response.token!!)
                Timber.i("Null value")
                null
            } else {
                Timber.i("errors not null")
                response.errors
            }
        }
    }

    suspend fun signIn(user: User): List<String>? {
        return withContext(Dispatchers.IO) {
            val response = userClient.signIn(user)

            if (response.condition) {
                insertUser(response.user!!, response.token!!)
                null
            } else {
                response.errors
            }
        }
    }

    suspend fun updatePhoto(token: String, mp: MultipartBody.Part) {
        withContext(Dispatchers.IO) {
            userClient.updatePhoto(AUTH_SCHEME.plus(token), mp)
        }
    }

    suspend fun updateName(token: String, name: String) {
        withContext(Dispatchers.IO) {
            userClient.updateName(AUTH_SCHEME.plus(token), name)
            sharedPreferences.edit {
                this.putString(NAME, name)
            }
        }
    }

    private fun insertUser(user: User, userToken: String) {
        sharedPreferences.edit {
            this.putString(TOKEN, token)
            this.putString(NAME, user.name)
            this.putString(USERNAME, user.username)
            this.putString(IMG_URL, user.imgUrl)
            apply()
        }

        token = userToken
    }
}