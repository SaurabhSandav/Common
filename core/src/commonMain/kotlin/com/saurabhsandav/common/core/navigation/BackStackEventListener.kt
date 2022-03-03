package com.saurabhsandav.common.core.navigation

public fun interface BackStackEventListener<ROUTE : Any> {

    public fun onChanged(event: BackStackEvent<ROUTE>)
}

public sealed interface BackStackEvent<ROUTE : Any> {

    public val entry: RouteEntry<ROUTE>

    public class RouteAdded<ROUTE : Any>(
        override val entry: RouteEntry<ROUTE>
    ) : BackStackEvent<ROUTE>

    public class RouteRemoved<ROUTE : Any>(
        override val entry: RouteEntry<ROUTE>
    ) : BackStackEvent<ROUTE>
}
