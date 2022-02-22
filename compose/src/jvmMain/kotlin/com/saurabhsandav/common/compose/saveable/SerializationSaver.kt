package com.saurabhsandav.common.compose.saveable

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import kotlinx.serialization.KSerializer

public actual fun <T : Any> serializableSaver(
    serializer: KSerializer<T>,
): Saver<T, Any> = autoSaver()
