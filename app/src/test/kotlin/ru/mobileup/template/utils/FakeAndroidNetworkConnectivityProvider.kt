package ru.mobileup.template.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.aartikov.replica.network.NetworkConnectivityProvider

class FakeAndroidNetworkConnectivityProvider : NetworkConnectivityProvider {

    private val _connectedFlow = MutableStateFlow(true)

    override val connectedFlow: StateFlow<Boolean> get() = _connectedFlow
}