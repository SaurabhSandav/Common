package com.saurabhsandav.compose.common.router

import androidx.compose.runtime.saveable.SaveableStateRegistry
import com.saurabhsandav.compose.common.presenter.Presenter
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer

internal class BackStack<T : Any>(
    startRoute: T,
    routeSerializer: KSerializer<T>,
    private val key: String,
    saveableStateRegistry: SaveableStateRegistry,
) : Presenter(saveableStateRegistry) {

    private val _current = saveableStateFlow(ListSerializer(routeSerializer)) { listOf(startRoute) }
    val current = _current.asStateFlow()

    private val listeners = mutableListOf<BackStackListener<T>>()

    fun transform(transformation: (List<T>) -> List<T>) {

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

    fun getEntryKey(route: T): String = getEntryKey(route, current.value)

    fun addListener(listener: BackStackListener<T>) {
        listeners.add(listener)
    }

    fun removeListener(listener: BackStackListener<T>) {
        listeners.remove(listener)
    }

    private fun getEntryKey(route: T, backStack: List<T>): String {
        return "${key}_${backStack.indexOf(route)}"
    }
}

internal interface BackStackListener<T : Any> {

    fun onAdded(route: T, routeKey: String)

    fun onRemoved(route: T, routeKey: String)
}
