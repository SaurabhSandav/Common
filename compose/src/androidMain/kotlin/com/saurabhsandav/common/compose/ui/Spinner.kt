package com.saurabhsandav.common.compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import com.saurabhsandav.common.compose.state

@Composable
actual fun <T> Spinner(
    items: List<T>,
    itemTitle: (T) -> String,
    selectedItem: T,
    onSelected: (T) -> Unit,
    modifier: Modifier,
) {

    val focusManager = LocalFocusManager.current
    var expanded by state { false }

    Box(modifier.onFocusChanged { if (it.isFocused) expanded = true }) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = itemTitle(selectedItem),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Show Options",
                )
            }
        )

        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                focusManager.clearFocus()
            },
        ) {

            items.forEach { item ->

                DropdownMenuItem(
                    onClick = {
                        onSelected(item)
                        focusManager.clearFocus()
                        expanded = false
                    },
                    content = { Text(text = itemTitle(item)) },
                )
            }
        }
    }
}
