package com.saurabhsandav.compose.common.router

import androidx.compose.runtime.saveable.SaveableStateRegistry
import com.saurabhsandav.compose.common.presenter.Presenter
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer

internal class BackStack<ROUTE : Any>(
    startRoute: ROUTE,
    routeSerializer: KSerializer<ROUTE>,
    private val key: String,
    saveableStateRegistry: SaveableStateRegistry,
) : Presenter(saveableStateRegistry) {

    private val _current = saveableStateFlow(ListSerializer(routeSerializer)) { listOf(startRoute) }
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

    fun getEntryKey(route: ROUTE): String = getEntryKey(route, current.value)

    fun addListener(listener: BackStackListener<ROUTE>) {
        listeners.add(listener)
    }

    fun removeListener(listener: BackStackListener<ROUTE>) {
        listeners.remove(listener)
    }

    private fun getEntryKey(route: ROUTE, backStack: List<ROUTE>): String {
        return "${key}_${backStack.indexOf(route)}"
    }
}

internal interface BackStackListener<ROUTE : Any> {

    fun onAdded(route: ROUTE, routeKey: String)

    fun onRemoved(route: ROUTE, routeKey: String)
}
