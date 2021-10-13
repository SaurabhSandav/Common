package com.saurabhsandav.common.compose.ui

import androidx.compose.runtime.Composable

@Composable
public expect fun <T> DropdownRadio(
    items: List<T>,
    itemTitle: (T) -> String,
    selectedItem: T,
    onSelect: (T) -> Unit,
    icon: @Composable () -> Unit,
)
