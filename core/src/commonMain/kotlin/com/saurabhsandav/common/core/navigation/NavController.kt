package com.saurabhsandav.common.core.navigation

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.toMutableStateList
import com.benasher44.uuid.uuid4

public class NavController<ROUTE : Any> private constructor(
    public val id: String,
    routeEntries: List<RouteEntry<ROUTE>>,
) {

    init {
        if (routeEntries.isEmpty())
            error("NavController needs an initial route")
    }

    private val _backStack = routeEntries.toMutableStateList()
    private val backStackEventListeners = mutableMapOf<String, BackStackEventListener<ROUTE>>()

    public val backStack: List<RouteEntry<ROUTE>>
        get() = _backStack
    public val resultHandler: ResultHandler = ResultHandler()

    public val canPop: Boolean by derivedStateOf { backStack.size >= 2 }

    public fun push(route: ROUTE) {

        val entry = RouteEntry(route)

        // Update Backstack
        _backStack.add(entry)

        // Notify listeners
        backStackEventListeners.values.forEach { listener ->
            listener.onChanged(BackStackEvent.RouteAdded(entry))
        }
    }

    public fun pop() {

        if (!canPop) return

        // Update Backstack
        val poppedRouteEntry = _backStack.removeLast()

        // Notify listeners
        backStackEventListeners.values.forEach { listener ->
            listener.onChanged(BackStackEvent.RouteRemoved(poppedRouteEntry))
        }
    }

    public fun addBackStackEventListener(
        key: String = uuid4().toString(),
        listener: BackStackEventListener<ROUTE>,
    ) {
        backStackEventListeners[key] = listener
    }

    public fun removeBackStackEventListener(listener: BackStackEventListener<ROUTE>) {
        backStackEventListeners.values.remove(listener)
    }

    public fun removeBackStackEventListener(key: String) {
        backStackEventListeners.remove(key)
    }

    public companion object {

        public operator fun <ROUTE : Any> invoke(
            initial: List<ROUTE>,
            id: String = uuid4().toString(),
        ): NavController<ROUTE> {
            return NavController(id, initial.map { RouteEntry(it) })
        }

        public fun <ROUTE : Any> restore(
            id: String,
            routeEntries: List<RouteEntry<ROUTE>>,
        ): NavController<ROUTE> {
            return NavController(id, routeEntries)
        }
    }
}
