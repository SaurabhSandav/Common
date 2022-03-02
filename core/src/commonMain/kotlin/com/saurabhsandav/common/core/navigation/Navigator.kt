package com.saurabhsandav.common.core.navigation

import androidx.compose.runtime.toMutableStateList

public class Navigator<ROUTE : Any>(initialRoutes: List<ROUTE>) {

    init {
        if (initialRoutes.isEmpty())
            error("Navigator needs an initial route")
    }

    private val listeners = mutableListOf<BackStackListener<ROUTE>>()
    private val _backStack = initialRoutes.toMutableStateList()
    private val backStackTransformer = BackStackTransformer(_backStack, listeners)

    public val backStack: List<ROUTE>
        get() = _backStack
    public val resultHandler: ResultHandler = ResultHandler()
    public val actions: NavigatorActions<ROUTE> = NavigatorActions(backStackTransformer, resultHandler)

    public fun addBackStackListener(listener: BackStackListener<ROUTE>) {
        listeners.add(listener)
    }

    public fun removeBackStackListener(listener: BackStackListener<ROUTE>) {
        listeners.remove(listener)
    }
}
