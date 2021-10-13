package com.saurabhsandav.common.compose.ui

import androidx.compose.runtime.Composable

@Composable
public expect fun Dialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
)
