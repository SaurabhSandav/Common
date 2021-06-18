package com.saurabhsandav.compose.common.router

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import com.saurabhsandav.compose.common.requireSaveableStateRegistry
import kotlinx.serialization.KSerializer

@Composable
public fun <T : Any> Navigator(
    startRoute: T,
    routeSerializer: KSerializer<T>,
    key: String = "RootRouter",
    content: @Composable NavigatorActions<T>.(T, RouterResultHandler) -> Unit,
) {

    val saveableStateRegistry = requireSaveableStateRegistry()
    val backStack = remember { BackStack(startRoute, routeSerializer, key, saveableStateRegistry) }
    val navigatorActions = remember { NavigatorActions(backStack) }
    val routerResultHandler = remember { RouterResultHandler() }

    BackHandler(backStack, navigatorActions)

    val currentRoute = backStack.current.collectAsState().value.last()

    WithSaveableState(currentRoute, backStack) {

        Crossfade(currentRoute) {
            navigatorActions.content(it, routerResultHandler)
        }
    }
}

@Composable
private fun <T : Any> WithSaveableState(
    currentRoute: T,
    backStack: BackStack<T>,
    content: @Composable () -> Unit,
) {

    val saveableStateHolder = rememberSaveableStateHolder()

    val onPopListener = remember {
        object : BackStackListener<T> {
            override fun onAdded(route: T, routeKey: String) {}

            override fun onRemoved(route: T, routeKey: String) {
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
