package com.saurabhsandav.common.core.navigation

public class NavigatorActions<ROUTE : Any> internal constructor(
    private val backStackTransformer: BackStackTransformer<ROUTE>,
    private val resultHandler: ResultHandler,
) {

    public fun push(
        newRoute: ROUTE,
        popWhile: (ROUTE) -> Boolean = { false },
    ) {
        backStackTransformer.transform {
            it.dropLastWhile(popWhile) + newRoute
        }
    }

    public fun pop() {

        backStackTransformer.transform {
            when {
                it.size > 1 -> it.dropLast(1)
                else -> it
            }
        }
    }

    public fun popWithResult(result: RouteResult) {

        resultHandler.setResult(result)

        pop()
    }
}

public fun <ROUTE : Any> NavigatorActions<ROUTE>.replace(newRoute: ROUTE) {

    var poppedLast = false

    push(newRoute) {
        if (!poppedLast) poppedLast = true
        true
    }
}
