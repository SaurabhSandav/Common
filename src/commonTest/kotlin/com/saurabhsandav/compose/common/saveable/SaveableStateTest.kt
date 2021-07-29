package com.saurabhsandav.compose.common.saveable

import androidx.compose.runtime.saveable.autoSaver
import com.saurabhsandav.compose.common.createRegistry
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
}
