package com.chatychaty.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.chatychaty.domain.model.Theme
import com.chatychaty.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val THEME_KEY = "Theme"
private const val TOKEN_KEY = "token"
private const val NAME_KEY = "name"
private const val USERNAME_KEY = "username"
private const val IMAGE_URL_KEY = "image_url"

class UserDaoImpl(private val store: DataStore<Preferences>) : UserDao {

    override val isUserSignedIn: Flow<Boolean>
        get() = store.data.map { preferences ->
            preferences[stringPreferencesKey(TOKEN_KEY)].let {
                when (it) {
                    null -> false
                    else -> true
                }
            }
        }

    override fun getUser(): Flow<User?> = store.data.map { preferences ->
        val name = preferences[stringPreferencesKey(NAME_KEY)]
        val username = preferences[stringPreferencesKey(USERNAME_KEY)]
        val imageUrl = preferences[stringPreferencesKey(IMAGE_URL_KEY)]
        if (name != null && username != null)
            User(name, username, password = null, imageUrl = imageUrl)
        else
            null
    }

    override fun getUserToken(): Flow<String> = store.data.map { preferences ->
        preferences[stringPreferencesKey(TOKEN_KEY)] as String
    }

    override suspend fun createOrUpdateUser(user: User, token: String?) {
        store.edit { preferences ->
            token?.let { preferences[stringPreferencesKey(TOKEN_KEY)] = it }
            user.imageUrl?.let { preferences[stringPreferencesKey(IMAGE_URL_KEY)] = it }
            preferences[stringPreferencesKey(NAME_KEY)] = user.name
            preferences[stringPreferencesKey(USERNAME_KEY)] = user.username
        }
    }

    override suspend fun deleteUser() {
        store.edit { preferences ->
            preferences.clear()
        }
    }

    override fun getTheme(): Flow<Theme> = store.data.map { preferences ->
        preferences[stringPreferencesKey(THEME_KEY)]?.let {
            Theme.valueOf(it)
        } ?: Theme.SYSTEM
    }

    override suspend fun updateTheme(value: Theme) {
        store.edit { preferences ->
            preferences[stringPreferencesKey(THEME_KEY)] = value.name
        }
    }
}