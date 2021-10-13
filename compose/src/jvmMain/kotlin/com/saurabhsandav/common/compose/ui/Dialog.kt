package com.saurabhsandav.common.compose.ui

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
