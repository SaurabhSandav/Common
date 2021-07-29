package com.saurabhsandav.compose.common

import androidx.compose.runtime.saveable.SaveableStateRegistry
import com.saurabhsandav.compose.common.navigator.BackStack
import com.saurabhsandav.compose.common.navigator.NavigatorActions
import kotlinx.serialization.builtins.serializer

internal fun createRegistry(
    restored: Map<String, List<Any?>>? = null,
) = SaveableStateRegistry(restored) { true }

internal fun createBackStack(
    initialRoute: String,
) = BackStack(
    initialRoutes = listOf(initialRoute),
    routeSerializer = String.serializer(),
    key = "Test",
    saveableStateRegistry = createRegistry()
)

internal fun createNavigatorActions(backStack: BackStack<String>) = NavigatorActions(backStack)
