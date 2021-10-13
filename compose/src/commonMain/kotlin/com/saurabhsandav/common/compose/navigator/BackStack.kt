package com.saurabhsandav.common.compose.navigator

import androidx.compose.runtime.saveable.SaveableStateRegistry
import com.saurabhsandav.common.compose.presenter.Presenter
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer

internal class BackStack<ROUTE : Any>(
    initialRoutes: List<ROUTE>,
    routeSerializer: KSerializer<ROUTE>,
    key: String,
    saveableStateRegistry: SaveableStateRegistry,
) : Presenter(saveableStateRegistry) {

    private val _current = saveableStateFlow(ListSerializer(routeSerializer), "BackStack_$key") { initialRoutes }
    val current = _current.asStateFlow()

    private val listeners = mutableListOf<BackStackListener<ROUTE>>()

    fun transform(transformation: (List<ROUTE>) -> List<ROUTE>) {

        val currentBackStack = current.value
        val transformedBackStack = transformation(currentBackStack)

        if (transformedBackStack.isEmpty()) error("Backstack cannot be empty")

        _current.value = transformedBackStack

        val removed = currentBackStack - transformedBackStack
        val added = transformedBackStack - currentBackStack

        listeners.forEach { listener ->
            removed.forEach { listener.onRemoved(it, getEntryKey(it, currentBackStack)) }
            added.forEach { listener.onAdded(it, getEntryKey(it, transformedBackStack)) }
        }
    }

    fun getEntryKey(route: ROUTE): Int = getEntryKey(route, current.value)

    fun addListener(listener: BackStackListener<ROUTE>) {
        listeners.add(listener)
    }

    fun removeListener(listener: BackStackListener<ROUTE>) {
        listeners.remove(listener)
    }

    private fun getEntryKey(route: ROUTE, backStack: List<ROUTE>): Int {
        return backStack.indexOf(route)
    }
}

internal interface BackStackListener<ROUTE : Any> {

    fun onAdded(route: ROUTE, key: Int)

    fun onRemoved(route: ROUTE, key: Int)
}
