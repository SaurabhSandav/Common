package com.saurabhsandav.common.compose.insets

import androidx.compose.ui.Modifier

public actual fun Modifier.systemBarsPadding(
    enabled: Boolean,
): Modifier = this

public actual fun Modifier.statusBarsPadding(): Modifier = this

public actual fun Modifier.navigationBarsPadding(
    bottom: Boolean,
    start: Boolean,
    end: Boolean,
): Modifier = this

public actual fun Modifier.imePadding(): Modifier = this

public actual fun Modifier.navigationBarsWithImePadding(): Modifier = this
