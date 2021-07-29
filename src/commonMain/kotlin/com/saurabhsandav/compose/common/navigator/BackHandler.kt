package com.saurabhsandav.compose.common.navigator

import androidx.compose.runtime.Composable

@Composable
internal expect fun BackHandler(
    enabled: Boolean,
    onBack: () -> Unit,
)
