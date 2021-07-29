package com.saurabhsandav.compose.common.navigator

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
