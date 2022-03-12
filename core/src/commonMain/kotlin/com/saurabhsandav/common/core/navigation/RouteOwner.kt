package com.saurabhsandav.common.core.navigation

/**
 * Provides platform specific functionality such as lifecycle, saved-sate etc. for routes in the backstack.
 */
public expect class RouteOwner<ROUTE : Any> : BackStackEventListener<ROUTE> {

    /**
     * Allows passing platform specific dependencies to build [RouteOwner].
     * Used by NavController to build a [RouteOwner] for every route in the backstack.
     */
    public class Builder {

        public fun <ROUTE : Any> build(
            navController: NavController<ROUTE>,
            routeEntry: RouteEntry<ROUTE>,
        ): RouteOwner<ROUTE>
    }
}
