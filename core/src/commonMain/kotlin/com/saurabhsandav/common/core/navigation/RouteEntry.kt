package com.saurabhsandav.common.core.navigation

import com.benasher44.uuid.uuid4
import kotlinx.serialization.Serializable

@Serializable
public data class RouteEntry<ROUTE : Any>(
    val key: ROUTE,
    val id: String = uuid4().toString(),
)
