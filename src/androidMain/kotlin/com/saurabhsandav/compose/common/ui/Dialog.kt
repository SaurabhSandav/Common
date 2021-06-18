package com.saurabhsandav.compose.common.ui

import androidx.compose.runtime.Composable

@Composable
actual fun Dialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismissRequest,
        content = content,
    )
}
