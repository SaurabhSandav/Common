package com.saurabhsandav.common.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.saurabhsandav.common.core.navigation.NavController
import com.saurabhsandav.common.core.navigation.PlatformOwner

@Composable
public fun <ROUTE : Any> rememberNavController(
    routeSaver: Saver<ROUTE, Any>? = null,
    // Why Radix? -> https://android-review.googlesource.com/c/platform/frameworks/support/+/1752326/
    id: String = currentCompositeKeyHash.toString(radix = 36),
    initial: () -> ROUTE,
): NavController<ROUTE> = rememberNavControllerPopulated(routeSaver, id) { listOf(initial()) }

@Composable
public fun <ROUTE : Any> rememberNavControllerPopulated(
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

    // Set PlatformOwnerBuilder
    val platformOwnerBuilder = rememberPlatformOwnerBuilder()
    controller.setPlatformOwnerBuilder(platformOwnerBuilder)

    return controller
}

@Composable
internal expect fun rememberPlatformOwnerBuilder(): PlatformOwner.Builder
