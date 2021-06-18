package com.saurabhsandav.compose.common.router

public class NavigatorActions<T : Any> internal constructor(
    private val backStack: BackStack<T>,
) {

    public fun push(newRoute: T) {
        backStack.transform {
            it.plus(newRoute)
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
