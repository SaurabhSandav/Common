package com.saurabhsandav.compose.common.router

import androidx.compose.runtime.Composable

@Composable
internal expect fun BackHandler(
    enabled: Boolean,
    onBack: () -> Unit,
)
