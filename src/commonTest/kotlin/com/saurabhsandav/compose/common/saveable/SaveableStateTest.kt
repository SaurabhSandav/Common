package com.saurabhsandav.compose.common.saveable

import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.runtime.saveable.autoSaver
import kotlin.test.Test
import kotlin.test.assertEquals

class SaveableStateTest {

    @Test
    fun testSaveableStateRegistryExtensions() {

        val saveableStateRegistry = createRegistry()

        val key = "TestKey"
        val saveStr = "Test String"

        saveableStateRegistry.registerProvider(key, autoSaver()) { saveStr }

        val saveMap = saveableStateRegistry.performSave()

        val restored = createRegistry(saveMap).consumeRestored(key, autoSaver()) { "New String" }

        assertEquals(saveStr, restored)
    }

    private fun createRegistry(
        restored: Map<String, List<Any?>>? = null,
    ) = SaveableStateRegistry(restored) { true }
}
