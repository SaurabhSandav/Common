package com.saurabhsandav.common.core.navigation

import com.benasher44.uuid.uuid4
import com.saurabhsandav.common.core.navigation.BackStackEvent.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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

    private val _backStack = MutableStateFlow(routeEntries)
    private val backStackEventListeners = mutableMapOf<String, BackStackEventListener<ROUTE>>()
    private var routeOwnerBuilder: RouteOwner.Builder? = null

    /**
     * [StateFlow] of [RouteEntries][RouteEntry].
     */
    public val backStack: StateFlow<List<RouteEntry<ROUTE>>> = _backStack.asStateFlow()

    /**
     * Pass results between routes.
     */
    public val resultHandler: ResultHandler = ResultHandler()

    /**
     * Can backstack be popped? True only when backstack has 2 or more entries.
     */
    public val canPop: Boolean
        get() = backStack.value.size >= 2

    /**
     * Transform backstack.
     *
     * * Sets a [RouteOwner] on newly added RouteEntries if [RouteOwner.Builder] is set.
     * * Dispatches [RouteAdded] and [RouteVisible] events to [BackStackEventListener].
     *
     * Note: If [transformation] returns an empty list, it is ignored.
     *
     * @param[transformation] Key for new route.
     */
    public fun transformBackStack(transformation: (List<RouteEntry<ROUTE>>) -> List<RouteEntry<ROUTE>>) {

        val current = backStack.value
        val transformed = transformation(backStack.value)

        if (transformed.isEmpty()) return

        val added = transformed - current.toSet()
        val removed = current - transformed.toSet()

        // Build transformation event list
        val events = buildList {
            addAll(removed.map { RouteRemoved(it) })
            addAll(added.map { RouteAdded(it) })
            add(RouteVisible(transformed.last()))
        }

        // Build and set RouteOwner on newly added RouteEntries
        routeOwnerBuilder?.run {
            added.forEach { entry ->
                entry.owner = build(this@NavController, entry)
            }
        }

        fun Collection<BackStackEventListener<ROUTE>>.notifyAll(events: List<BackStackEvent<ROUTE>>) {
            events.forEach { event ->
                forEach { listener -> listener.onChanged(event) }
            }
        }

        // Notify RouteOwners before backstack update
        backStack.value.mapNotNull { it.owner }.notifyAll(events)

        // Update Backstack
        _backStack.value = transformed

        // Notify listeners
        backStackEventListeners.values.notifyAll(events)
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
        backStack.value.forEach { entry -> entry.owner = builder.build(this, entry) }
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
