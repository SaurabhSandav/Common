package com.saurabhsandav.common.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.saurabhsandav.common.core.navigation.NavController
import com.saurabhsandav.common.core.navigation.RouteOwner

/**
 * Create and remember a [NavController] instance.
 *
 * * Handles saving NavController state.
 * * Sets a [RouteOwner.Builder] on [NavController].
 *
 * @param[routeSaver] Used to save [ROUTE] instance. If not provided, NavController is not saved.
 * @param[id] Unique id. Optionally auto-generated.
 * @param[initial] Initial route key.
 */
@Composable
public fun <ROUTE : Any> rememberNavController(
    initial: ROUTE,
    routeSaver: Saver<ROUTE, Any>? = null,
    // Why Radix? -> https://android-review.googlesource.com/c/platform/frameworks/support/+/1752326/
    id: String = currentCompositeKeyHash.toString(radix = 36),
): NavController<ROUTE> = rememberNavController(routeSaver, id) { listOf(initial) }

/**
 * Create and remember a [NavController] instance, pre-populated with multiple routes.
 *
 * * Handles saving NavController state.
 * * Sets a [RouteOwner.Builder] on [NavController].
 *
 * @param[routeSaver] Used to save [ROUTE] instance. If not provided, NavController is not saved.
 * @param[id] Unique id. Optionally auto-generated.
 * @param[initial] Provide/Build initial route keys.
 */
@Composable
public fun <ROUTE : Any> rememberNavController(
    routeSaver: Saver<ROUTE, Any>? = null,
    // Why Radix? -> https://android-review.googlesource.com/c/platform/frameworks/support/+/1752326/
    id: String = currentCompositeKeyHash.toString(radix = 36),
    initial: () -> List<ROUTE>,
): NavController<ROUTE> {

    val saver = remember(routeSaver) {
        when {
            routeSaver != null -> NavControllerSaver(routeSaver)
            else -> autoSaver()
        }
    }

    val controller = rememberSaveable(saver = saver) { NavController(initial(), id) }

    // Set RouteOwnerBuilder
    val routeOwnerBuilder = rememberRouteOwnerBuilder()
    controller.setRouteOwnerBuilder(routeOwnerBuilder)

    return controller
}

@Composable
internal expect fun rememberRouteOwnerBuilder(): RouteOwner.Builder
