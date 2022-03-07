package com.saurabhsandav.common.core.navigation

public actual class RouteOwner<ROUTE : Any> : BackStackEventListener<ROUTE> {

    override fun onChanged(event: BackStackEvent<ROUTE>) {}

    public actual class Builder {

        public actual fun <ROUTE : Any> build(
            navController: NavController<ROUTE>,
            routeEntry: RouteEntry<ROUTE>,
        ): RouteOwner<ROUTE> = RouteOwner()
    }
}
