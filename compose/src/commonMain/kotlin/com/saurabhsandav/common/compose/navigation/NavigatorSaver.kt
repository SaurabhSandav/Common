package com.saurabhsandav.common.compose.navigation

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.mapSaver
import com.saurabhsandav.common.core.navigation.Navigator
import com.saurabhsandav.common.core.navigation.RouteEntry

@Suppress("FunctionName")
internal fun <ROUTE : Any> NavigatorSaver(
    routeSaver: Saver<ROUTE, Any>
): Saver<Navigator<ROUTE>, Any> {

    val routeEntrySaver = RouteEntrySaver(routeSaver)

    return listSaver(
        save = { navigator ->

            navigator.backStack.map { entry ->
                with(routeEntrySaver) {
                    SaverScope { true }.save(entry)!!
                }
            }
        },
        restore = { restored ->

            val routes = restored.map {
                routeEntrySaver.restore(it)!!
            }

            Navigator.restore(routes)
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
