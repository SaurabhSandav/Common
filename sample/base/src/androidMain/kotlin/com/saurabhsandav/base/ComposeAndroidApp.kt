package com.saurabhsandav.base

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets

fun ComponentActivity.ComposeAndroidApp() {

    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent {
        ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
            App()
        }
    }
}
