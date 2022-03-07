package com.saurabhsandav.common.core.navigation

import com.benasher44.uuid.uuid4
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
public data class RouteEntry<ROUTE : Any>(
    val key: ROUTE,
    val id: String = uuid4().toString(),
) {

    @Transient
    var owner: RouteOwner<ROUTE>? = null
        internal set
}
