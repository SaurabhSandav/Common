package com.saurabhsandav.common.core.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

public class Navigator<ROUTE : Any>(initialRoutes: List<ROUTE>) {

    init {
        if (initialRoutes.isEmpty())
            error("Navigator needs an initial route")
    }

    private val listeners = mutableListOf<BackStackListener<ROUTE>>()
    private val _backStack = MutableStateFlow(initialRoutes)
    private val backStackTransformer = BackStackTransformer(_backStack, listeners)

    public val backStack: StateFlow<List<ROUTE>> = _backStack.asStateFlow()
    public val resultHandler: ResultHandler = ResultHandler()
    public val actions: NavigatorActions<ROUTE> = NavigatorActions(backStackTransformer, resultHandler)

    public fun addBackStackListener(listener: BackStackListener<ROUTE>) {
        listeners.add(listener)
    }

    public fun removeBackStackListener(listener: BackStackListener<ROUTE>) {
        listeners.remove(listener)
    }
}
