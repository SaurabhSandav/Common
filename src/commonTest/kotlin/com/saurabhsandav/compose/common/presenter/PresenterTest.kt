package com.saurabhsandav.compose.common.presenter

import androidx.compose.runtime.saveable.SaveableStateRegistry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.Test
import kotlin.test.assertEquals

class PresenterTest {

    @Test
    fun testSavedDelegate() {

        val saveableStateRegistry = createRegistry()

        val testStr = "Test String"
        val testInt = 1234

        TestPresenter(saveableStateRegistry).apply {
            saved(testStr)
            saved(testInt)
        }

        val saveMap = saveableStateRegistry.performSave()

        TestPresenter(createRegistry(saveMap)).apply {
            assertEquals(testStr, saved("Fail"))
            assertEquals(testInt, saved(5678))
        }
    }

    @Test
    fun testSaveableStateFlow() {

        val saveableStateRegistry = createRegistry()

        val initialFlowValue = "Flow initialized"
        val updatedFlowValue = "Flow updated"

        TestPresenter(saveableStateRegistry).apply {
            val flow = saveableStateFlow { initialFlowValue }
            flow.value = updatedFlowValue
        }

        val saveMap = saveableStateRegistry.performSave()

        TestPresenter(createRegistry(saveMap)).apply {

            val flow = saveableStateFlow { initialFlowValue }

            assertEquals(updatedFlowValue, flow.value)
        }
    }

    private fun createRegistry(
        restored: Map<String, List<Any?>>? = null,
    ) = SaveableStateRegistry(restored) { true }

    private class TestPresenter(
        saveableStateRegistry: SaveableStateRegistry
    ) : Presenter(saveableStateRegistry) {

        inline fun <reified T : Any> saved(toSave: T): T {
            val saved by saved { toSave }
            return saved
        }

        inline fun <reified T : Any> saveableStateFlow(noinline initialValue: () -> T): MutableStateFlow<T> {
            // Make sure to call super class method
            return saveableStateFlow(null, initialValue)
        }
    }
}
