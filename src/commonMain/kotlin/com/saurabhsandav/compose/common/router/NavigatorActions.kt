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

    public fun pop(): Boolean {

        var result = false

        backStack.transform {
            when {
                it.size > 1 -> {
                    result = true
                    it.dropLast(1)
                }
                else -> it
            }
        }

        return result
    }
}
