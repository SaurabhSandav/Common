package com.saurabhsandav.common.compose.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import com.saurabhsandav.common.core.navigation.BackStackEvent
import com.saurabhsandav.common.core.navigation.BackStackEventListener
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

    BackHandler(
        enabled = navigator.canPop,
        onBack = { navigator.pop() }
    )

    val currentRouteEntry = navigator.backStack.last()

    WithSaveableState(currentRouteEntry.id, navigator) {

        Crossfade(currentRouteEntry) {
            navigator.content(it.key, navigator.resultHandler.consumeResult())
        }
    }
}

@Composable
private fun <ROUTE : Any> WithSaveableState(
    routeId: String,
    navigator: Navigator<ROUTE>,
    content: @Composable () -> Unit,
) {

    val saveableStateHolder = rememberSaveableStateHolder()

    DisposableEffect(navigator) {

        val onRouteRemovedListener = BackStackEventListener<ROUTE> { event ->
            if (event !is BackStackEvent.RouteRemoved) return@BackStackEventListener
            saveableStateHolder.removeState(event.entry.id)
        }

        navigator.addBackStackEventListener(listener = onRouteRemovedListener)
        onDispose { navigator.removeBackStackEventListener(onRouteRemovedListener) }
    }

    saveableStateHolder.SaveableStateProvider(
        key = routeId,
        content = content,
    )
}
