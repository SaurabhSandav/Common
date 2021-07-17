package com.saurabhsandav.compose.common.presenter

import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.saveable.SaveableStateRegistry
import com.saurabhsandav.compose.common.saveable.PlatformSaver
import com.saurabhsandav.compose.common.saveable.consumeRestored
import com.saurabhsandav.compose.common.saveable.registerProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

public abstract class Presenter(
    private val saveableStateRegistry: SaveableStateRegistry
) : RememberObserver {

    private val saveableProviderEntries = mutableListOf<SaveableStateRegistry.Entry>()

    protected val viewModelScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    protected inline fun <reified T : Any> save(
        key: String? = null,
        noinline block: () -> T,
    ): T = save(serializer(), key, block)

    protected fun <T : Any> save(
        serializer: KSerializer<T>,
        key: String? = null,
        block: () -> T,
    ): T = with(saveableStateRegistry) {

        val finalKey = key ?: createSaveableKey()

        val saver = PlatformSaver(serializer)

        val value = consumeRestored(finalKey, saver, block)
        val entry = registerProvider(finalKey, saver) { value }

        saveableProviderEntries.add(entry)

        return value
    }

    protected inline fun <reified T : Any> saveableStateFlow(
        key: String? = null,
        noinline initialValue: () -> T,
    ): MutableStateFlow<T> = saveableStateFlow(serializer(), key, initialValue)

    protected fun <T : Any> saveableStateFlow(
        serializer: KSerializer<T>,
        key: String? = null,
        initialValue: () -> T,
    ): MutableStateFlow<T> {

        val finalKey = key ?: createSaveableKey()

        val saver = PlatformSaver(serializer)

        val value = saveableStateRegistry.consumeRestored(finalKey, saver, initialValue)

        val stateFlow = MutableStateFlow(value)

        val entry = saveableStateRegistry.registerProvider(finalKey, saver) { stateFlow.value }

        saveableProviderEntries.add(entry)

        return stateFlow
    }

    private fun createSaveableKey(): String {
        return "${this@Presenter::class.qualifiedName}_${saveableProviderEntries.size}"
    }

    private fun onDispose() {

        viewModelScope.cancel()

        saveableProviderEntries.forEach { it.unregister() }
        saveableProviderEntries.clear()
    }

    override fun onRemembered() {
    }

    override fun onAbandoned() {
        onDispose()
    }

    override fun onForgotten() {
        onDispose()
    }
}
