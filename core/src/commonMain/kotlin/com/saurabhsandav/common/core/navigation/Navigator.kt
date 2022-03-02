package com.saurabhsandav.common.core.navigation

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.toMutableStateList

public class Navigator<ROUTE : Any>(initialRoutes: List<ROUTE>) {

    init {
        if (initialRoutes.isEmpty())
            error("Navigator needs an initial route")
    }

    private val listeners = mutableListOf<BackStackListener<ROUTE>>()
    private val _backStack = initialRoutes.toMutableStateList()

    public val backStack: List<ROUTE>
        get() = _backStack
    public val resultHandler: ResultHandler = ResultHandler()

    public val canPop: Boolean by derivedStateOf { backStack.size >= 2 }

    public fun push(route: ROUTE) {

        // Update Backstack
        _backStack.add(route)

        val addedIndex = backStack.lastIndex

        // Notify listeners
        listeners.forEach { listener ->
            listener.onAdded(addedIndex, route)
        }
    }

    public fun pop() {

        if (!canPop) return

        // Update Backstack
        val removedIndex = backStack.lastIndex
        val removedRoute = _backStack.removeLast()

        // Notify listeners
        listeners.forEach { listener ->
            listener.onRemoved(removedIndex, removedRoute)
        }
    }

    public fun addBackStackListener(listener: BackStackListener<ROUTE>) {
        listeners.add(listener)
    }

    public fun removeBackStackListener(listener: BackStackListener<ROUTE>) {
        listeners.remove(listener)
    }
}
