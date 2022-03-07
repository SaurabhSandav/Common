package com.saurabhsandav.common.core.navigation

public expect class RouteOwner<ROUTE : Any> : BackStackEventListener<ROUTE> {

    public class Builder {

        public fun <ROUTE : Any> build(
            navController: NavController<ROUTE>,
            routeEntry: RouteEntry<ROUTE>,
        ): RouteOwner<ROUTE>
    }
}
