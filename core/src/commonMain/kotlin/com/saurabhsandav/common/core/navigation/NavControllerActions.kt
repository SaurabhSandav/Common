package com.saurabhsandav.common.core.navigation

/**
 * Push a new [route] on top of backstack.
 *
 * @param[popWhile] Optional. Pop routes while condition is satisfied.
 */
public fun <ROUTE : Any> NavController<ROUTE>.push(
    route: ROUTE,
    popWhile: (ROUTE) -> Boolean = { false },
) {

    while (popWhile(backStack.value.last().key)) {
        pop()
    }

    push(route)
}

/**
 * Set a result and pop current route from the backstack.
 */
public fun <ROUTE : Any> NavController<ROUTE>.popWithResult(result: RouteResult) {

    resultHandler.setResult(result)

    pop()
}

/**
 * Replace current route in the backstack.
 */
public fun <ROUTE : Any> NavController<ROUTE>.replace(newRoute: ROUTE) {
    pop()
    push(newRoute)
}
