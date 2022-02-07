package com.saurabhsandav.common.core.navigation

import kotlinx.coroutines.flow.MutableStateFlow

internal class BackStackTransformer<ROUTE : Any>(
    private val _backStack: MutableStateFlow<List<ROUTE>>,
    private val listeners: List<BackStackListener<ROUTE>>,
) {

    internal fun transform(transformation: (List<ROUTE>) -> List<ROUTE>) {

        val currentBackStack = _backStack.value
        val transformedBackStack = transformation(currentBackStack)

        if (transformedBackStack.isEmpty()) error("Backstack cannot be empty")

        _backStack.value = transformedBackStack

        val removed = currentBackStack - transformedBackStack.toSet()
        val added = transformedBackStack - currentBackStack.toSet()

        listeners.forEach { listener ->
            removed.forEach { listener.onRemoved(currentBackStack.indexOf(it), it) }
            added.forEach { listener.onAdded(transformedBackStack.indexOf(it), it) }
        }
    }
}
