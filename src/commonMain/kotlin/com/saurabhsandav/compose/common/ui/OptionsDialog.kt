package com.saurabhsandav.compose.common.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable

@Composable
public fun OptionsDialog(
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {

    Dialog(onDismissRequest = onDismissRequest) {

        Surface(color = MaterialTheme.colors.background) {

            Column(content = content)
        }
    }
}
