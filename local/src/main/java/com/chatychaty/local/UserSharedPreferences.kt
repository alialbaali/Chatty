package com.chatychaty.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.chatychaty.data.source.local.UserLocalDataSource
import com.chatychaty.domain.model.User
import com.chatychaty.domain.repository.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UserSharedPreferences(private val sp: SharedPreferences) : UserLocalDataSource {

    override fun getUser(): Flow<User> {
        val name = sp.getString(NAME, String()) as String
        val username = sp.getString(USERNAME, String()) as String
        val imgUrl = sp.getString(IMG_URL, null)
        val user = User(name = name, username = username, imgUrl = imgUrl)
        return flowOf(user)
    }

    override fun getUserToken(): String {
        return sp.getString(TOKEN, String()) as String
    }

    override suspend fun createUser(user: User, token: String) {
        sp.edit {
            this.putString(TOKEN, token)
            this.putString(NAME, user.name)
            this.putString(USERNAME, user.username)
            this.putString(IMG_URL, user.imgUrl)
            apply()
        }
    }

    override suspend fun setUserValue(key: String, value: String) {
        sp.edit().putString(key, value).apply()
    }

    override suspend fun deleteUser() {
        sp.edit().clear().apply()
    }

    override suspend fun getThemeValue(): String {
        return sp.getString(THEME, null) ?: SYSTEM_DEFAULT
    }

    override suspend fun setThemeValue(value: String) {
        sp.edit().putString(THEME, value).apply()
    }
}