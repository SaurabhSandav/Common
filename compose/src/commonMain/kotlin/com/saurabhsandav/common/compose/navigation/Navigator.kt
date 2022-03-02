package com.saurabhsandav.common.compose.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.*
import com.saurabhsandav.common.core.navigation.BackStackListener
import com.saurabhsandav.common.core.navigation.Navigator
import com.saurabhsandav.common.core.navigation.RouteResult

@Composable
public fun <ROUTE : Any> Navigator(
    vararg initialRoutes: ROUTE,
    routeSaver: Saver<ROUTE, Any>? = null,
    content: @Composable Navigator<ROUTE>.(ROUTE, RouteResult?) -> Unit,
) {

    val saver = remember(routeSaver) {
        when {
            routeSaver != null -> NavigatorSaver(routeSaver)
            else -> autoSaver()
        }
    }
    val navigator = rememberSaveable(saver = saver) { Navigator(initialRoutes.toList()) }

    val backStack = navigator.backStack
    val currentRoute = backStack.last()

    BackHandler(
        enabled = navigator.canPop,
        onBack = { navigator.pop() }
    )

    WithSaveableState(navigator) {

        Crossfade(currentRoute) {
            navigator.content(it, navigator.resultHandler.consumeResult())
        }
    }
}

@Composable
private fun <ROUTE : Any> WithSaveableState(
    navigator: Navigator<ROUTE>,
    content: @Composable () -> Unit,
) {

    val backStack = navigator.backStack
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

@Suppress("FunctionName")
internal fun <ROUTE : Any> NavigatorSaver(
    routeSaver: Saver<ROUTE, Any>
): Saver<Navigator<ROUTE>, Any> {
    return listSaver(
        save = { navigator ->

            navigator.backStack.map {
                with(routeSaver) {
                    SaverScope { true }.save(it) ?: error("Could not save route")
                }
            }
        },
        restore = { restored ->

            val routes = restored.map {
                routeSaver.restore(it) ?: error("Could not restore saved route")
            }

            Navigator(routes)
        },
    )
}
