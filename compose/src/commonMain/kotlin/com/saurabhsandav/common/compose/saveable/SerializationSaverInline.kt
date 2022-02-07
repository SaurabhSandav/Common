package com.saurabhsandav.common.compose.saveable

import androidx.compose.runtime.saveable.Saver
import kotlinx.serialization.serializer

// Can't move reified serializableSaver() to SerializationSaver file.
// Causes Duplicate JVM class name error.
// https://youtrack.jetbrains.com/issue/KT-21186

public inline fun <reified T : Any> serializableSaver(): Saver<T, Any> {
    return serializableSaver(serializer = serializer())
}
