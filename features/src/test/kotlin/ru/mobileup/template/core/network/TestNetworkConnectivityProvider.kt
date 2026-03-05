package ru.mobileup.template.core.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.aartikov.replica.network.NetworkConnectivityProvider

class TestNetworkConnectivityProvider : NetworkConnectivityProvider {

    private val _connectedFlow = MutableStateFlow(true)

    override val connectedFlow: StateFlow<Boolean>
        get() = _connectedFlow

    var connected by _connectedFlow::value
}
