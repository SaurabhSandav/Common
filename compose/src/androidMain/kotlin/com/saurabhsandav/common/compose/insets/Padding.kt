package com.saurabhsandav.common.compose.insets

import androidx.compose.ui.Modifier
import com.google.accompanist.insets.imePadding as accompanistImePadding
import com.google.accompanist.insets.navigationBarsPadding as accompanistNavigationBarsPadding
import com.google.accompanist.insets.navigationBarsWithImePadding as accompanistNavigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding as accompanistStatusBarsPadding
import com.google.accompanist.insets.systemBarsPadding as accompanistSystemBarsPadding

public actual fun Modifier.systemBarsPadding(
    enabled: Boolean,
): Modifier = accompanistSystemBarsPadding(enabled)

public actual fun Modifier.statusBarsPadding(): Modifier = accompanistStatusBarsPadding()

public actual fun Modifier.navigationBarsPadding(
    bottom: Boolean,
    start: Boolean,
    end: Boolean,
): Modifier = accompanistNavigationBarsPadding(
    bottom = bottom,
    start = start,
    end = end
)

public actual fun Modifier.imePadding(): Modifier = accompanistImePadding()

public actual fun Modifier.navigationBarsWithImePadding(): Modifier = accompanistNavigationBarsWithImePadding()
