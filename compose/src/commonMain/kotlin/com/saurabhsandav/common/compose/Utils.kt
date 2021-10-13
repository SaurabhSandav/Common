package com.saurabhsandav.common.compose

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
public inline fun <T> state(value: @DisallowComposableCalls () -> T): MutableState<T> {
    return remember { mutableStateOf(value()) }
}

@Composable
public inline fun <T> state(
    key1: Any?,
    value: @DisallowComposableCalls () -> T
): MutableState<T> {
    return remember(key1) { mutableStateOf(value()) }
}

@Composable
public fun requireSaveableStateRegistry(): SaveableStateRegistry {
    return LocalSaveableStateRegistry.current ?: error("SaveableStateRegistry is cannot be null")
}

@Composable
public inline fun <T> saveableState(crossinline value: @DisallowComposableCalls () -> T): MutableState<T> {
    return rememberSaveable { mutableStateOf(value()) }
}
