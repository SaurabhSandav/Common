package com.saurabhsandav.common.compose.navigator

import androidx.compose.runtime.Composable

@Composable
internal expect fun BackHandler(
    enabled: Boolean,
    onBack: () -> Unit,
)
