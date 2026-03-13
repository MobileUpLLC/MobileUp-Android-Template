package ru.mobileup.template.core_testing.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.aartikov.replica.network.NetworkConnectivityProvider

class TestNetworkConnectivityProvider : NetworkConnectivityProvider {

    private val _connectedFlow = MutableStateFlow(true)
    override val connectedFlow: StateFlow<Boolean> = _connectedFlow

    var isConnected: Boolean
        get() = _connectedFlow.value
        set(value) { _connectedFlow.value = value }
}
