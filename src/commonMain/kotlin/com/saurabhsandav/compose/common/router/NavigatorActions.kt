package com.saurabhsandav.compose.common.router

public class NavigatorActions<T : Any> internal constructor(
    private val backStack: BackStack<T>,
) {

    public fun push(
        newRoute: T,
        popWhile: (T) -> Boolean = { false },
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

public fun <T : Any> NavigatorActions<T>.replace(newRoute: T) {

    var poppedLast = false

    push(newRoute) {
        if (!poppedLast) poppedLast = true
        true
    }
}
