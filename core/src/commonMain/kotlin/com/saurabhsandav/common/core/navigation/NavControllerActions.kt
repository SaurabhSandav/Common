package com.saurabhsandav.common.core.navigation

import androidx.compose.runtime.snapshots.Snapshot

public fun <ROUTE : Any> NavController<ROUTE>.push(
    route: ROUTE,
    popWhile: (ROUTE) -> Boolean = { false },
) {

    Snapshot.withMutableSnapshot {

        while (popWhile(backStack.last().key)) {
            pop()
        }

        push(route)
    }
}

public fun <ROUTE : Any> NavController<ROUTE>.popWithResult(result: RouteResult) {

    resultHandler.setResult(result)

    pop()
}

public fun <ROUTE : Any> NavController<ROUTE>.replace(newRoute: ROUTE) {
    Snapshot.withMutableSnapshot {
        pop()
        push(newRoute)
    }
}
