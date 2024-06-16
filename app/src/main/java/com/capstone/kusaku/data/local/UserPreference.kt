package com.capstone.kusaku.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserSession) {
        dataStore.edit { preferences ->
            user.token?.let { preferences[TOKEN_KEY] = it }
            user.username?.let { preferences[USERNAME_KEY] = it }
            user.email?.let { preferences[EMAIL_KEY] = it }
            user.income?.let { preferences[INCOME_KEY] = it }
            user.refreshToken?.let { preferences[REFRESH_TOKEN_KEY] = it }
        }
    }

    fun getSession(): Flow<UserSession> {
        return dataStore.data.map { preferences ->
            UserSession(
                token = preferences[TOKEN_KEY],
                username = preferences[USERNAME_KEY],
                email = preferences[EMAIL_KEY],
                income = preferences[INCOME_KEY],
                refreshToken = preferences[REFRESH_TOKEN_KEY]
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val TOKEN_KEY = stringPreferencesKey("token")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val INCOME_KEY = stringPreferencesKey("income")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}