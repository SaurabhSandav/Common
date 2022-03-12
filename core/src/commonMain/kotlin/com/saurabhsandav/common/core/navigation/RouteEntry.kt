package com.saurabhsandav.common.core.navigation

import com.benasher44.uuid.uuid4
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Collection of properties associated with a route [key].
 *
 * @param[key] User provided route key.
 * @param[id] Auto generated unique id identifying an instance of route.
 */
@Serializable
public data class RouteEntry<ROUTE : Any>(
    val key: ROUTE,
    val id: String = uuid4().toString(),
) {

    /**
     * [RouteOwner] associated with a Route.
     * Provides platform specific functionality such as lifecycle, saved-sate etc. for this route.
     */
    @Transient
    var owner: RouteOwner<ROUTE>? = null
        internal set
}
