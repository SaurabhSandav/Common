package com.saurabhsandav.common.compose.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import com.saurabhsandav.common.compose.saveable.serializableSaver
import com.saurabhsandav.common.core.navigation.BackStackListener
import com.saurabhsandav.common.core.navigation.Navigator
import com.saurabhsandav.common.core.navigation.NavigatorActions
import com.saurabhsandav.common.core.navigation.RouteResult
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer

@Composable
public fun <ROUTE : Any> Navigator(
    vararg initialRoutes: ROUTE,
    routeSerializer: KSerializer<ROUTE>,
    content: @Composable NavigatorActions<ROUTE>.(ROUTE, RouteResult?) -> Unit,
) {

    val saver = remember { NavigatorSaver(routeSerializer) }
    val navigator = rememberSaveable(saver = saver) { Navigator(initialRoutes.toList()) }

    val backStack by navigator.backStack.collectAsState()
    val currentRoute = backStack.last()

    BackHandler(
        enabled = backStack.size > 1,
        onBack = { navigator.actions.pop() }
    )

    WithSaveableState(navigator) {

        Crossfade(currentRoute) {
            navigator.actions.content(it, navigator.resultHandler.consumeResult())
        }
    }
}

@Composable
private fun <ROUTE : Any> WithSaveableState(
    navigator: Navigator<ROUTE>,
    content: @Composable () -> Unit,
) {

    val backStack by navigator.backStack.collectAsState()
    val currentRoute = backStack.last()

    // Why Radix? -> https://android-review.googlesource.com/c/platform/frameworks/support/+/1752326/
    val rootKey = currentCompositeKeyHash.toString(36)
    val currentRouteIndex = backStack.indexOf(currentRoute)

    val saveableStateHolder = rememberSaveableStateHolder()

    val onPopListener = remember {
        object : BackStackListener<ROUTE> {
            override fun onAdded(index: Int, route: ROUTE) {}

            override fun onRemoved(index: Int, route: ROUTE) {
                saveableStateHolder.removeState("${rootKey}_$index")
            }
        }
    }

    DisposableEffect(navigator, onPopListener) {
        navigator.addBackStackListener(onPopListener)
        onDispose { navigator.removeBackStackListener(onPopListener) }
    }

    saveableStateHolder.SaveableStateProvider(
        key = "${rootKey}_$currentRouteIndex",
        content = content,
    )
}

private class NavigatorSaver<ROUTE : Any>(
    routeSerializer: KSerializer<ROUTE>
) : Saver<Navigator<ROUTE>, Any> {

    val backStackSaver = serializableSaver(ListSerializer(routeSerializer))

    override fun SaverScope.save(value: Navigator<ROUTE>): Any? {
        return with(backStackSaver) {
            SaverScope { true }.save(value.backStack.value)
        }
    }

    override fun restore(value: Any): Navigator<ROUTE>? {
        return backStackSaver.restore(value)?.let { routes -> Navigator(routes) }
    }
}
