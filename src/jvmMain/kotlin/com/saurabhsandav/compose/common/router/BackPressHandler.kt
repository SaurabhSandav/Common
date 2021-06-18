package com.saurabhsandav.compose.common.router

import androidx.compose.runtime.Composable

@Composable
internal actual fun <T : Any> BackHandler(
    backStack: BackStack<T>,
    navigatorActions: NavigatorActions<T>,
) {
}
