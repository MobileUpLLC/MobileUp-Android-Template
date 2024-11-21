package ru.mobileup.template.core.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.CoroutineDispatcher

class AndroidSettingsFactory(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher
) : SettingsFactory {

    override fun createSettings(name: String): Settings {
        return AndroidSettings(createSharedPreferences(name), dispatcher)
    }

    override fun createEncryptedSettings(name: String): Settings {
        return AndroidSettings(createEncryptedSharedPreferences(name), dispatcher)
    }

    private fun createSharedPreferences(name: String): SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    private fun createEncryptedSharedPreferences(name: String): SharedPreferences {
        return EncryptedSharedPreferences.create(
            name,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}
