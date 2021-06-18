package com.saurabhsandav.compose.common.ui

import androidx.compose.runtime.Composable

@Composable
public actual fun Dialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {

    androidx.compose.ui.window.Dialog(
        onCloseRequest = onDismissRequest,
        content = {
            content()
        },
    )
}
