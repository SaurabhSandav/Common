package com.saurabhsandav.common.compose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saurabhsandav.common.compose.state

@Composable
actual fun <T> DropdownRadio(
    items: List<T>,
    itemTitle: (T) -> String,
    selectedItem: T,
    onSelect: (T) -> Unit,
    icon: @Composable () -> Unit,
) {

    Box {

        var expanded by state { false }

        IconButton(
            onClick = { expanded = !expanded },
            content = icon
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {

            items.forEach { item ->

                val selected = selectedItem == item
                val onClick = { onSelect(item) }

                DropdownMenuItem(onClick) {

                    Row(
                        modifier = Modifier.selectable(
                            selected = selected,
                            onClick = onClick
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {

                        RadioButton(
                            selected = selected,
                            onClick = onClick,
                        )

                        Text(text = itemTitle(item))
                    }
                }
            }
        }
    }
}
