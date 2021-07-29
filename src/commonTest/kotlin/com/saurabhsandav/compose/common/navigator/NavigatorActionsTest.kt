package com.saurabhsandav.compose.common.navigator

import com.saurabhsandav.compose.common.createBackStack
import com.saurabhsandav.compose.common.createNavigatorActions
import kotlin.test.Test
import kotlin.test.assertEquals

class NavigatorActionsTest {

    @Test
    fun testPush() {

        val backStack = createBackStack("Initial Route")
        val navigatorActions = createNavigatorActions(backStack)

        val newRoute = "New Route"

        navigatorActions.push(newRoute)

        assertEquals(newRoute, backStack.current.value.last())
    }

    @Test
    fun testPushAndPopWhile() {

        val initialRoute = "Initial Route"
        val newRoute = "New Route"

        val backStack = createBackStack("Initial Route")
        val navigatorActions = createNavigatorActions(backStack)

        navigatorActions.push("Route #2")
        navigatorActions.push("Route #3")
        navigatorActions.push(newRoute) { it != initialRoute }

        assertEquals(listOf(initialRoute, newRoute), backStack.current.value)
    }

    @Test
    fun testPop() {

        val initialRoute = "Initial Route"

        val backStack = createBackStack(initialRoute)
        val navigatorActions = createNavigatorActions(backStack)

        navigatorActions.push("New Route")
        navigatorActions.pop()

        assertEquals(initialRoute, backStack.current.value.last())
    }

    @Test
    fun testReplace() {

        val initialRoute = "Initial Route"

        val backStack = createBackStack(initialRoute)
        val navigatorActions = createNavigatorActions(backStack)

        navigatorActions.push("Route to replace")

        val replacementRoute = "Replacement Route"
        navigatorActions.replace(replacementRoute)

        assertEquals(replacementRoute, backStack.current.value.last())
    }
}
