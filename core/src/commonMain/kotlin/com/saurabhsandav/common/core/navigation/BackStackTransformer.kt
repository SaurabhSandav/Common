package com.saurabhsandav.common.core.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList

internal class BackStackTransformer<ROUTE : Any>(
    private val _backStack: SnapshotStateList<ROUTE>,
    private val listeners: List<BackStackListener<ROUTE>>,
) {

    internal fun transform(transformation: (List<ROUTE>) -> List<ROUTE>) {

        val currentBackStack = _backStack
        val transformedBackStack = transformation(currentBackStack)

        if (transformedBackStack.isEmpty()) error("Backstack cannot be empty")

        _backStack.clear()
        _backStack.addAll(transformedBackStack)

        val removed = currentBackStack - transformedBackStack.toSet()
        val added = transformedBackStack - currentBackStack.toSet()

        listeners.forEach { listener ->
            removed.forEach { listener.onRemoved(currentBackStack.indexOf(it), it) }
            added.forEach { listener.onAdded(transformedBackStack.indexOf(it), it) }
        }
    }
}
