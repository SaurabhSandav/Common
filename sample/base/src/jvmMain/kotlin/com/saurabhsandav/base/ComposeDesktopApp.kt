package com.saurabhsandav.base

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.saurabhsandav.common.compose.saveable.WithSaveableState

fun ComposeDesktopApp() {

    application {

        Window(onCloseRequest = ::exitApplication) {

            WithSaveableState(key = "Desktop") {

                App()
            }
        }
    }
}
