package com.saurabhsandav.common.core.navigation

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.benasher44.uuid.uuid4
import com.saurabhsandav.common.core.navigation.BackStackEvent.*

/**
 * Pure Kotlin implementation of Navigation.
 *
 * Features:
 * * Navigation implementation is UI agnostic.
 * * Pass results between routes with [ResultHandler].
 * * Observe backstack events with [BackStackEventListener].
 * * Provide platform specific functionality for routes using [RouteOwner].
 */
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
    private var routeOwnerBuilder: RouteOwner.Builder? = null

    /**
     * [SnapshotStateList] of [RouteEntries][RouteEntry].
     */
    public val backStack: List<RouteEntry<ROUTE>>
        get() = _backStack

    /**
     * Pass results between routes.
     */
    public val resultHandler: ResultHandler = ResultHandler()

    /**
     * Can backstack be popped? True only when backstack has 2 or more entries.
     */
    public val canPop: Boolean by derivedStateOf { backStack.size >= 2 }

    /**
     * Push new [route] on the backstack.
     *
     * * Creates a new [RouteEntry] for [route].
     * * Sets a [RouteOwner] on [RouteEntry] if [setRouteOwnerBuilder] was called.
     * * Dispatches [RouteAdded] and [RouteVisible] events to [BackStackEventListener].
     *
     * @param[route] Key for new route.
     */
    public fun push(route: ROUTE) {

        val entry = RouteEntry(route)

        // Build and set RouteOwner on RouteEntry
        entry.owner = routeOwnerBuilder?.build(this, entry)

        // Notify RouteOwners before backstack modification
        backStack.mapNotNull { it.owner }.notifyAll(
            RouteAdded(entry),
            RouteVisible(entry),
        )

        // Update Backstack
        _backStack.add(entry)

        // Notify listeners
        backStackEventListeners.values.notifyAll(
            RouteAdded(entry),
            RouteVisible(entry),
        )
    }


    /**
     * Pop current route from the backstack.
     * Dispatches [RouteRemoved] and [RouteVisible] events to [BackStackEventListener].
     */
    public fun pop() {

        if (!canPop) return

        val poppedRouteEntry = _backStack.last()
        val nextVisibleRouteEntry = backStack[backStack.lastIndex - 1]

        // Notify RouteOwners before backstack modification
        backStack.mapNotNull { it.owner }.notifyAll(
            RouteRemoved(poppedRouteEntry),
            RouteVisible(nextVisibleRouteEntry),
        )

        // Update Backstack
        _backStack.removeLast()

        // Notify listeners
        backStackEventListeners.values.notifyAll(
            RouteRemoved(poppedRouteEntry),
            RouteVisible(nextVisibleRouteEntry),
        )
    }

    /**
     * Add a [BackStackEventListener] to listen to dispatched [BackStackEvents][BackStackEvent].
     *
     * @param[key] Optional key. Can be used with [removeBackStackEventListener].
     * @param[listener] An instance of [BackStackEventListener].
     */
    public fun addBackStackEventListener(
        key: String = uuid4().toString(),
        listener: BackStackEventListener<ROUTE>,
    ) {
        backStackEventListeners[key] = listener
    }

    /**
     * Remove previously added [BackStackEventListener] instance.
     */
    public fun removeBackStackEventListener(listener: BackStackEventListener<ROUTE>) {
        backStackEventListeners.values.remove(listener)
    }

    /**
     * Remove the [BackStackEventListener] identified by [key].
     */
    public fun removeBackStackEventListener(key: String) {
        backStackEventListeners.remove(key)
    }

    /**
     * Set a new [RouteOwner.Builder]. Runs for all existing routes and every newly added route.
     */
    public fun setRouteOwnerBuilder(builder: RouteOwner.Builder) {

        if (routeOwnerBuilder == builder) return

        routeOwnerBuilder = builder

        // Run builder for existing routes
        backStack.forEach { entry -> entry.owner = builder.build(this, entry) }
    }

    private fun Collection<BackStackEventListener<ROUTE>>.notifyAll(vararg events: BackStackEvent<ROUTE>) {
        forEach { listener -> events.forEach { event -> listener.onChanged(event) } }
    }

    public companion object {

        /**
         * Create a new instance of NavController.
         *
         * @param[initial] Initial route keys.
         * @param[id] Optional. Unique id.
         */
        public operator fun <ROUTE : Any> invoke(
            initial: List<ROUTE>,
            id: String = uuid4().toString(),
        ): NavController<ROUTE> {
            return NavController(id, initial.map { RouteEntry(it) })
        }

        /**
         * Restore saved NavController instance.
         *
         * @param[id] Unique id of saved [NavController] instance.
         * @param[routeEntries] Restored backstack [RouteEntry] list.
         */
        public fun <ROUTE : Any> restore(
            id: String,
            routeEntries: List<RouteEntry<ROUTE>>,
        ): NavController<ROUTE> {
            return NavController(id, routeEntries)
        }
    }
}
