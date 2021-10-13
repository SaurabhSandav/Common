package com.saurabhsandav.common.compose.saveable

import androidx.compose.runtime.saveable.Saver
import kotlinx.serialization.serializer

public inline fun <reified T : Any> PlatformSaver(): Saver<T, Any> {
    return PlatformSaver(serializer = serializer())
}
