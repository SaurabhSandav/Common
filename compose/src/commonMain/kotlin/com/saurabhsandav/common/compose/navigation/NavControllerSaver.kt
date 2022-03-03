package com.saurabhsandav.common.compose.navigation

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.mapSaver
import com.saurabhsandav.common.core.navigation.NavController
import com.saurabhsandav.common.core.navigation.RouteEntry

@Suppress("FunctionName")
internal fun <ROUTE : Any> NavControllerSaver(
    routeSaver: Saver<ROUTE, Any>
): Saver<NavController<ROUTE>, Any> {

    val routeEntrySaver = RouteEntrySaver(routeSaver)

    return listSaver(
        save = { navController ->

            val saved = mutableListOf<Any>()

            saved.add(navController.id)

            navController.backStack.mapTo(saved) { entry ->
                with(routeEntrySaver) {
                    SaverScope { true }.save(entry)!!
                }
            }

            saved
        },
        restore = { restored ->

            val id = restored.first() as String

            val routes = restored.drop(1).map {
                routeEntrySaver.restore(it)!!
            }

            NavController.restore(id, routes)
        },
    )
}

@Suppress("FunctionName")
private fun <ROUTE : Any> RouteEntrySaver(
    routeSaver: Saver<ROUTE, Any>
): Saver<RouteEntry<ROUTE>, Any> {
    return mapSaver(
        save = { entry ->

            mapOf(
                "key" to with(routeSaver) {
                    SaverScope { true }.save(entry.key) ?: error("Could not save route")
                },
                "id" to entry.id,
            )
        },
        restore = { restored ->

            RouteEntry(
                key = routeSaver.restore(restored["key"]!!) ?: error("Could not restore saved route"),
                id = restored["id"]!! as String,
            )
        },
    )
}
