package com.saurabhsandav.compose.common.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
internal actual fun <T : Any> BackHandler(
    backStack: BackStack<T>,
    navigatorActions: NavigatorActions<T>,
) {

    val currentBackStack by backStack.current.collectAsState()

    androidx.activity.compose.BackHandler(
        enabled = currentBackStack.size > 1,
        onBack = { navigatorActions.pop() }
    )
}
