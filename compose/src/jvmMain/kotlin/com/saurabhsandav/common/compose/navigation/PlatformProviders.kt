package com.saurabhsandav.common.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.remember
import com.saurabhsandav.common.core.navigation.RouteEntry
import com.saurabhsandav.common.core.navigation.RouteOwner

@Composable
internal actual fun rememberRouteOwnerBuilder(): RouteOwner.Builder {
    return remember { RouteOwner.Builder() }
}

internal actual fun RouteEntry<*>.buildPlatformProviders(): Array<ProvidedValue<*>> {
    return emptyArray()
}
