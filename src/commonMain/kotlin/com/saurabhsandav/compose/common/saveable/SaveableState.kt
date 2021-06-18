package com.saurabhsandav.compose.common.saveable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveableStateHolder

@Composable
public inline fun <reified T : Any> WithSaveableState(
    key: T,
    noinline content: @Composable () -> Unit,
) {
    rememberSaveableStateHolder().SaveableStateProvider(key, content)
}

public fun <Original, Saveable : Any> SaveableStateRegistry.registerProvider(
    key: String,
    saver: Saver<Original, Saveable>,
    canBeSaved: (Any) -> Boolean = this::canBeSaved,
    toSave: () -> Original,
) {

    val saverScope = SaverScope(canBeSaved)

    registerProvider(key) {
        with(saver) {
            with(saverScope) {
                save(toSave())
            }
        }
    }
}

public fun <Original, Saveable : Any> SaveableStateRegistry.consumeRestored(
    key: String,
    saver: Saver<Original, Saveable>,
    default: () -> Original,
): Original {
    @Suppress("UNCHECKED_CAST")
    val simpleRestored = consumeRestored(key) as Saveable?
    val restored = simpleRestored?.let { saver.restore(it) }
    return restored ?: default()
}
