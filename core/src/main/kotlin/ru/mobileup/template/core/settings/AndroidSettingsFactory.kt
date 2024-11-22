package ru.mobileup.template.core.settings

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.CoroutineDispatcher

internal class AndroidSettingsFactory(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
) : SettingsFactory {

    private val defaultSettings = mutableMapOf<String, AndroidSettings>()

    private val encryptedSettings = mutableMapOf<String, AndroidSettings>()

    override fun createSettings(
        name: String,
    ): Settings = defaultSettings.getOrPut(name) {
        AndroidSettings(
            sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE),
            dispatcher = dispatcher
        )
    }

    override fun createEncryptedSettings(
        name: String,
    ): Settings = encryptedSettings.getOrPut(name) {
        AndroidSettings(
            sharedPreferences = EncryptedSharedPreferences(
                context = context,
                fileName = name,
                masterKey = MasterKey(context = context),
            ),
            dispatcher = dispatcher
        )
    }
}
