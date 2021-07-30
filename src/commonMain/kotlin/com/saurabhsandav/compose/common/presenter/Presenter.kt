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
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

public abstract class Presenter(
    private val saveableStateRegistry: SaveableStateRegistry
) : RememberObserver {

    private val saveableProviderEntries = mutableListOf<SaveableStateRegistry.Entry>()

    protected val presenterScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    protected inline fun <T, reified V : Any> saved(
        key: String? = null,
        noinline block: () -> V,
    ): ReadWriteProperty<T, V> = saved(serializer(), key, block)

    protected fun <T, V : Any> saved(
        serializer: KSerializer<V>,
        key: String? = null,
        block: () -> V,
    ): ReadWriteProperty<T, V> = with(saveableStateRegistry) {

        val finalKey = key ?: createSaveableKey()

        val saver = PlatformSaver(serializer)

        return@with object : ReadWriteProperty<T, V> {

            private var value = consumeRestored(finalKey, saver, block)

            init {

                val entry = registerProvider(finalKey, saver) { value }

                saveableProviderEntries.add(entry)
            }

            override fun getValue(thisRef: T, property: KProperty<*>): V = value

            override fun setValue(thisRef: T, property: KProperty<*>, value: V) {
                this.value = value
            }
        }
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

        presenterScope.cancel()

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
