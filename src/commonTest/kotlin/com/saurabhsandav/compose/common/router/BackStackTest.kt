package com.saurabhsandav.compose.common.router

import androidx.compose.runtime.saveable.SaveableStateRegistry
import kotlinx.serialization.builtins.serializer
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
            override fun onAdded(route: String, routeKey: String) {}
            override fun onRemoved(route: String, routeKey: String) {}
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

            override fun onAdded(route: String, routeKey: String) {
                added = route
            }

            override fun onRemoved(route: String, routeKey: String) {
                removed = route
            }
        }

        backStack.addListener(listener)

        backStack.transform { it + newRoute }
        assertEquals(added, newRoute)

        backStack.transform { it.dropLast(1) }
        assertEquals(removed, newRoute)
    }

    private fun createBackStack(
        initialRoute: String,
    ) = BackStack(
        startRoute = initialRoute,
        routeSerializer = String.serializer(),
        key = "Test",
        saveableStateRegistry = createRegistry()
    )

    private fun createRegistry(
        restored: Map<String, List<Any?>>? = null,
    ) = SaveableStateRegistry(restored) { true }
}
