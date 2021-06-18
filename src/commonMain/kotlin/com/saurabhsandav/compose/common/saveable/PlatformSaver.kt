package com.saurabhsandav.compose.common.saveable

import androidx.compose.runtime.saveable.Saver
import kotlinx.serialization.KSerializer

public expect fun <T : Any> PlatformSaver(serializer: KSerializer<T>): Saver<T, Any>
