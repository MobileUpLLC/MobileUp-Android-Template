package ru.mobileup.template.core.settings

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AndroidSettings(
    private val sharedPreferences: SharedPreferences,
    private val dispatcher: CoroutineDispatcher
) : Settings {

    override suspend fun getString(key: String): String? = withContext(dispatcher) {
        sharedPreferences.getString(key, null)
    }

    override suspend fun getLong(key: String): Long? = withContext(dispatcher) {
        val value = sharedPreferences.getLong(key, 0)
        if (sharedPreferences.contains(key)) value else null
    }

    override suspend fun getBoolean(key: String): Boolean? = withContext(dispatcher) {
        val value = sharedPreferences.getBoolean(key, false)
        if (sharedPreferences.contains(key)) value else null
    }

    override suspend fun getInt(key: String): Int? = withContext(dispatcher) {
        val value = sharedPreferences.getInt(key, 0)
        if (sharedPreferences.contains(key)) value else null
    }

    override suspend fun getFloat(key: String): Float? = withContext(dispatcher) {
        val value = sharedPreferences.getFloat(key, 0f)
        if (sharedPreferences.contains(key)) value else null
    }

    override suspend fun putString(key: String, value: String) = withContext(dispatcher) {
        sharedPreferences.edit {
            putString(key, value)
        }
    }

    override suspend fun putLong(key: String, value: Long) = withContext(dispatcher) {
        sharedPreferences.edit {
            putLong(key, value)
        }
    }

    override suspend fun putBoolean(key: String, value: Boolean) = withContext(dispatcher) {
        sharedPreferences.edit {
            putBoolean(key, value)
        }
    }

    override suspend fun putInt(key: String, value: Int) = withContext(dispatcher) {
        sharedPreferences.edit {
            putInt(key, value)
        }
    }

    override suspend fun putFloat(key: String, value: Float) = withContext(dispatcher) {
        sharedPreferences.edit {
            putFloat(key, value)
        }
    }

    override suspend fun remove(key: String) = withContext(dispatcher) {
        sharedPreferences.edit {
            remove(key)
        }
    }
}
