package com.saurabhsandav.compose.common.router

import com.saurabhsandav.compose.common.createBackStack
import kotlin.test.Test
import kotlin.test.assertEquals

class BackStackTest {

    @Test
    fun testFlowUpdate() {

        val initialRoute = "Initial Route"
        val newRoute = "New Route"

        val backStack = createBackStack(initialRoute)

        backStack.transform { it + newRoute }

        assertEquals(listOf(initialRoute, newRoute), backStack.current.value)
    }

    @Test
    fun testObserveBackStack() {

        val backStack = createBackStack("Initial Route")

        val listener = object : BackStackListener<String> {
            override fun onAdded(route: String, key: Int) {}
            override fun onRemoved(route: String, key: Int) {}
        }

        backStack.addListener(listener)

        backStack.removeListener(listener)
    }

    @Test
    fun testListener() {

        val initialRoute = "Initial Route"
        val newRoute = "New Route"

        val backStack = createBackStack(initialRoute)

        var added: String? = null
        var removed: String? = null

        val listener = object : BackStackListener<String> {

            override fun onAdded(route: String, key: Int) {
                added = route
            }

            override fun onRemoved(route: String, key: Int) {
                removed = route
            }
        }

        backStack.addListener(listener)

        backStack.transform { it + newRoute }
        assertEquals(added, newRoute)

        backStack.transform { it.dropLast(1) }
        assertEquals(removed, newRoute)
    }
}
