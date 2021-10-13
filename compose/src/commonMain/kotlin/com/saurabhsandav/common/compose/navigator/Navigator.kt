package com.saurabhsandav.common.compose.navigator

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import com.saurabhsandav.common.compose.requireSaveableStateRegistry
import kotlinx.serialization.KSerializer

@Composable
public fun <ROUTE : Any> Navigator(
    initialRoute: ROUTE,
    routeSerializer: KSerializer<ROUTE>,
    content: @Composable NavigatorActions<ROUTE>.(ROUTE, RouteResult?) -> Unit,
) {

    Navigator(listOf(initialRoute), routeSerializer, content)
}

@Composable
public fun <ROUTE : Any> Navigator(
    initialRoutes: List<ROUTE>,
    routeSerializer: KSerializer<ROUTE>,
    content: @Composable NavigatorActions<ROUTE>.(ROUTE, RouteResult?) -> Unit,
) {

    LaunchedEffect(initialRoutes) {
        if (initialRoutes.isEmpty())
            error("Navigator needs an initial route")
    }

    val saveableStateRegistry = requireSaveableStateRegistry()
    // Why Radix? -> https://android-review.googlesource.com/c/platform/frameworks/support/+/1752326/
    val key: String = currentCompositeKeyHash.toString(36)
    val backStack = remember { BackStack(initialRoutes, routeSerializer, key, saveableStateRegistry) }
    val resultHandler = remember { ResultHandler() }
    val navigatorActions = remember { NavigatorActions(backStack, resultHandler) }

    val currentBackStack by backStack.current.collectAsState()
    val currentRoute = currentBackStack.last()

    BackHandler(
        enabled = currentBackStack.size > 1,
        onBack = { navigatorActions.pop() }
    )

    WithSaveableState(currentRoute, backStack, key) {

        Crossfade(currentRoute) {
            navigatorActions.content(it, resultHandler.consumeResult())
        }
    }
}

@Composable
private fun <ROUTE : Any> WithSaveableState(
    currentRoute: ROUTE,
    backStack: BackStack<ROUTE>,
    navigatorKey: String,
    content: @Composable () -> Unit,
) {

    val saveableStateHolder = rememberSaveableStateHolder()

    val onPopListener = remember {
        object : BackStackListener<ROUTE> {
            override fun onAdded(route: ROUTE, key: Int) {}

            override fun onRemoved(route: ROUTE, key: Int) {
                saveableStateHolder.removeState(generateKey(navigatorKey, key))
            }
        }
    }

    DisposableEffect(backStack, onPopListener) {
        backStack.addListener(onPopListener)
        onDispose { backStack.removeListener(onPopListener) }
    }

    val key = generateKey(navigatorKey, backStack.getEntryKey(currentRoute))

    saveableStateHolder.SaveableStateProvider(key, content)
}

private fun generateKey(navigatorKey: String, routeKey: Int): String = "${navigatorKey}_$routeKey"
