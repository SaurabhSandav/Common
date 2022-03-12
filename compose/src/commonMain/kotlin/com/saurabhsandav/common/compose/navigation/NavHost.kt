package com.saurabhsandav.common.compose.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import com.saurabhsandav.common.core.navigation.*

/**
 * Configure Navigation in compose using a [NavController] instance.
 *
 * * Provides appropriate CompositionLocals based on platform if [RouteOwner.Builder] is set on [NavController].
 * * Pops routes on back press.
 *
 * @param[controller] An instance of [NavController].
 * @param[content] Composable block to build UI based on provided [RouteEntry] and [RouteResult].
 */
@Composable
public fun <ROUTE : Any> NavHost(
    controller: NavController<ROUTE>,
    content: @Composable (RouteEntry<ROUTE>, RouteResult?) -> Unit,
) {

    BackHandler(
        enabled = controller.canPop,
        onBack = { controller.pop() },
    )

    val currentRouteEntry = controller.backStack.last()

    val platformProviders = remember(currentRouteEntry) { currentRouteEntry.buildPlatformProviders() }

    CompositionLocalProvider(*platformProviders) {
        WithSaveableState(currentRouteEntry.id, controller) {
            content(currentRouteEntry, controller.resultHandler.consumeResult())
        }
    }
}

internal expect fun RouteEntry<*>.buildPlatformProviders(): Array<ProvidedValue<*>>

@Composable
private fun <ROUTE : Any> WithSaveableState(
    routeId: String,
    controller: NavController<ROUTE>,
    content: @Composable () -> Unit,
) {

    val saveableStateHolder = rememberSaveableStateHolder()

    DisposableEffect(controller) {

        val onRouteRemovedListener = BackStackEventListener<ROUTE> { event ->
            if (event !is BackStackEvent.RouteRemoved) return@BackStackEventListener
            saveableStateHolder.removeState(event.entry.id)
        }

        controller.addBackStackEventListener(listener = onRouteRemovedListener)
        onDispose { controller.removeBackStackEventListener(onRouteRemovedListener) }
    }

    saveableStateHolder.SaveableStateProvider(routeId, content)
}
