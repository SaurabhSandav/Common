package com.saurabhsandav.common.compose.navigator

import androidx.compose.runtime.Composable

@Composable
internal actual fun BackHandler(
    enabled: Boolean,
    onBack: () -> Unit,
) {

    androidx.activity.compose.BackHandler(
        enabled = enabled,
        onBack = onBack,
    )
}
