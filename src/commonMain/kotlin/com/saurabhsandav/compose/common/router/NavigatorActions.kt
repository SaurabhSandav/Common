package com.saurabhsandav.compose.common.router

public class NavigatorActions<ROUTE : Any> internal constructor(
    private val backStack: BackStack<ROUTE>,
) {

    public fun push(
        newRoute: ROUTE,
        popWhile: (ROUTE) -> Boolean = { false },
    ) {
        backStack.transform {
            it.dropLastWhile(popWhile) + newRoute
        }
    }

    public fun pop() {

        backStack.transform {
            when {
                it.size > 1 -> it.dropLast(1)
                else -> it
            }
        }
    }
}

public fun <ROUTE : Any> NavigatorActions<ROUTE>.replace(newRoute: ROUTE) {

    var poppedLast = false

    push(newRoute) {
        if (!poppedLast) poppedLast = true
        true
    }
}
