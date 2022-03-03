package com.saurabhsandav.common.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.remember
import com.saurabhsandav.common.core.navigation.PlatformOwner
import com.saurabhsandav.common.core.navigation.RouteEntry

@Composable
internal actual fun rememberPlatformOwnerBuilder(): PlatformOwner.Builder {
    return remember { PlatformOwner.Builder() }
}

internal actual fun RouteEntry<*>.buildPlatformProviders(): Array<ProvidedValue<*>> {
    return emptyArray()
}
