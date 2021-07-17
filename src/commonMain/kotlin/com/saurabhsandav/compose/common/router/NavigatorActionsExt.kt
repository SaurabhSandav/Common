package com.saurabhsandav.compose.common.router

public fun <T : Any> NavigatorActions<T>.replace(newRoute: T) {

    var poppedLast = false

    push(newRoute) {
        if (!poppedLast) poppedLast = true
        true
    }
}
