package com.saurabhsandav.common.core.navigation

public fun interface BackStackEventListener<ROUTE : Any> {

    public fun onChanged(event: BackStackEvent<ROUTE>)
}

public sealed interface BackStackEvent<ROUTE : Any> {

    /**
     * [RouteEntry] associated with the current event.
     */
    public val entry: RouteEntry<ROUTE>

    /**
     * Route added to backstack.
     */
    public class RouteAdded<ROUTE : Any>(
        override val entry: RouteEntry<ROUTE>
    ) : BackStackEvent<ROUTE>

    /**
     * Route removed from backstack.
     */
    public class RouteRemoved<ROUTE : Any>(
        override val entry: RouteEntry<ROUTE>
    ) : BackStackEvent<ROUTE>

    /**
     * Route is visible.
     */
    public class RouteVisible<ROUTE : Any>(
        override val entry: RouteEntry<ROUTE>
    ) : BackStackEvent<ROUTE>
}
