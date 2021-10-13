package com.saurabhsandav.common.compose.insets

import androidx.compose.ui.Modifier
import com.google.accompanist.insets.imePadding as accompanistImePadding
import com.google.accompanist.insets.navigationBarsPadding as accompanistNavigationBarsPadding
import com.google.accompanist.insets.navigationBarsWithImePadding as accompanistNavigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding as accompanistStatusBarsPadding
import com.google.accompanist.insets.systemBarsPadding as accompanistSystemBarsPadding

actual fun Modifier.systemBarsPadding(
    enabled: Boolean,
): Modifier = accompanistSystemBarsPadding(enabled)

actual fun Modifier.statusBarsPadding(): Modifier = accompanistStatusBarsPadding()

actual fun Modifier.navigationBarsPadding(
    bottom: Boolean,
    start: Boolean,
    end: Boolean,
): Modifier = accompanistNavigationBarsPadding(
    bottom = bottom,
    start = start,
    end = end
)

actual fun Modifier.imePadding(): Modifier = accompanistImePadding()

actual fun Modifier.navigationBarsWithImePadding(): Modifier = accompanistNavigationBarsWithImePadding()
