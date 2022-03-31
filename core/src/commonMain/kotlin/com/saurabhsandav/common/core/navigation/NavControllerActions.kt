package com.saurabhsandav.common.core.navigation

/**
 * Push a new [route] on top of backstack.
 *
 * @param[popWhile] Optional. Pop routes while condition is met.
 */
public fun <ROUTE : Any> NavController<ROUTE>.push(
    route: ROUTE,
    popWhile: (ROUTE) -> Boolean = { false },
) {
    transformBackStack { current ->
        current.dropLastWhile { popWhile(it.key) } + RouteEntry(route)
    }
}

/**
 * Pop current route from the backstack.
 */
public fun <ROUTE : Any> NavController<ROUTE>.pop() {
    transformBackStack { current -> current.dropLast(1) }
}

/**
 * Set a result and pop current route from the backstack.
 */
public fun <ROUTE : Any> NavController<ROUTE>.popWithResult(result: RouteResult) {

    resultHandler.setResult(result)

    pop()
}

/**
 * Replace current route in the backstack with [newRoute].
 */
public fun <ROUTE : Any> NavController<ROUTE>.replace(newRoute: ROUTE) {
    transformBackStack { current ->
        current.dropLast(1) + RouteEntry(newRoute)
    }
}
