package com.saurabhsandav.compose.common.ui

import androidx.compose.runtime.Composable

@Composable
public expect fun Dialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
)
