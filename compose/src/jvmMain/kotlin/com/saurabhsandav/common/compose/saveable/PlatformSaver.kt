package com.saurabhsandav.common.compose.saveable

import androidx.compose.runtime.saveable.Saver
import kotlinx.serialization.KSerializer

public actual fun <T : Any> PlatformSaver(
    serializer: KSerializer<T>,
): Saver<T, Any> = Saver(
    save = { it },
    restore = {
        @Suppress("UNCHECKED_CAST")
        it as T
    },
)
