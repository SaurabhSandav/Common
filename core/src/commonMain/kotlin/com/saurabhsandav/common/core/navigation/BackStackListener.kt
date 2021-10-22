package com.saurabhsandav.common.core.navigation

public interface BackStackListener<ROUTE : Any> {

    public fun onAdded(index: Int, route: ROUTE)

    public fun onRemoved(index: Int, route: ROUTE)
}
