package com.saurabhsandav.common.compose.saveable

import android.os.Bundle
import androidx.compose.runtime.saveable.Saver
import dev.ahmedmourad.bundlizer.bundle
import dev.ahmedmourad.bundlizer.unbundle
import kotlinx.serialization.KSerializer

actual fun <T : Any> PlatformSaver(
    serializer: KSerializer<T>,
): Saver<T, Any> = Saver(
    save = { it.bundle(serializer) },
    restore = { (it as Bundle).unbundle(serializer) },
)
