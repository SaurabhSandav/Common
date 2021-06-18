package com.saurabhsandav.compose.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public expect fun <T> Spinner(
    items: List<T>,
    itemTitle: (T) -> String,
    selectedItem: T,
    onSelected: (T) -> Unit,
    modifier: Modifier,
)
