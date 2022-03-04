package com.saurabhsandav.common.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saurabhsandav.common.core.navigation.PlatformOwner
import com.saurabhsandav.common.core.navigation.RouteEntry
import com.saurabhsandav.common.core.navigation.ViewModelStoreHolder

@Composable
internal actual fun rememberPlatformOwnerBuilder(): PlatformOwner.Builder {

    val hostLifecycleOwner = LocalLifecycleOwner.current
    val hostSavedStateRegistryOwner = LocalSavedStateRegistryOwner.current
    val context = LocalContext.current
    val viewModelStoreHolder = viewModel<ViewModelStoreHolder>()

    return remember {
        PlatformOwner.Builder(
            context = context,
            hostLifecycle = hostLifecycleOwner.lifecycle,
            hostSavedStateRegistry = hostSavedStateRegistryOwner.savedStateRegistry,
            viewModelStoreHolder = viewModelStoreHolder,
        )
    }
}

internal actual fun RouteEntry<*>.buildPlatformProviders(): Array<ProvidedValue<*>> {

    val platformOwner = platformOwner ?: return emptyArray()

    return arrayOf(
        LocalLifecycleOwner provides platformOwner,
        LocalSavedStateRegistryOwner provides platformOwner,
        LocalViewModelStoreOwner provides platformOwner,
    )
}
