package com.saurabhsandav.common.core.navigation

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.toMutableStateList

public class Navigator<ROUTE : Any> private constructor(routeEntries: List<RouteEntry<ROUTE>>) {

    init {
        if (routeEntries.isEmpty())
            error("Navigator needs an initial route")
    }

    private val _backStack = routeEntries.toMutableStateList()
    private val listeners = mutableListOf<BackStackListener<ROUTE>>()

    public val backStack: List<RouteEntry<ROUTE>>
        get() = _backStack
    public val resultHandler: ResultHandler = ResultHandler()

    public val canPop: Boolean by derivedStateOf { backStack.size >= 2 }

    public fun push(route: ROUTE) {

        // Update Backstack
        _backStack.add(RouteEntry(route))

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
        val removedRoute = _backStack.removeLast().key

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

    public companion object {

        public operator fun <ROUTE : Any> invoke(initial: List<ROUTE>): Navigator<ROUTE> {
            return Navigator(initial.map { RouteEntry(it) })
        }

        public fun <ROUTE : Any> restore(routeEntries: List<RouteEntry<ROUTE>>): Navigator<ROUTE> {
            return Navigator(routeEntries)
        }
    }
}
