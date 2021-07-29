package com.saurabhsandav.compose.common.router

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import com.saurabhsandav.compose.common.requireSaveableStateRegistry
import kotlinx.serialization.KSerializer

@Composable
public fun <ROUTE : Any> Navigator(
    startRoute: ROUTE,
    routeSerializer: KSerializer<ROUTE>,
    key: String = "RootRouter",
    content: @Composable NavigatorActions<ROUTE>.(ROUTE, RouterResultHandler) -> Unit,
) {

    val saveableStateRegistry = requireSaveableStateRegistry()
    val backStack = remember { BackStack(startRoute, routeSerializer, key, saveableStateRegistry) }
    val navigatorActions = remember { NavigatorActions(backStack) }
    val routerResultHandler = remember { RouterResultHandler() }

    val currentBackStack by backStack.current.collectAsState()
    val currentRoute = currentBackStack.last()

    BackHandler(
        enabled = currentBackStack.size > 1,
        onBack = { navigatorActions.pop() }
    )

    WithSaveableState(currentRoute, backStack) {

        Crossfade(currentRoute) {
            navigatorActions.content(it, routerResultHandler)
        }
    }
}

@Composable
private fun <ROUTE : Any> WithSaveableState(
    currentRoute: ROUTE,
    backStack: BackStack<ROUTE>,
    content: @Composable () -> Unit,
) {

    val saveableStateHolder = rememberSaveableStateHolder()

    val onPopListener = remember {
        object : BackStackListener<ROUTE> {
            override fun onAdded(route: ROUTE, routeKey: String) {}

            override fun onRemoved(route: ROUTE, routeKey: String) {
                saveableStateHolder.removeState(routeKey)
            }
        }
    }

    DisposableEffect(backStack, onPopListener) {
        backStack.addListener(onPopListener)
        onDispose { backStack.removeListener(onPopListener) }
    }

    saveableStateHolder.SaveableStateProvider(backStack.getEntryKey(currentRoute), content)
}
