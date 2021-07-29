package com.saurabhsandav.compose.common.navigator

import kotlin.test.Test
import kotlin.test.assertEquals

class RouteResultTest {

    @Test
    fun test() {

        val resultHandler = ResultHandler()

        val testResult = object : RouteResult {}

        assertEquals(null, resultHandler.consumeResult())

        resultHandler.setResult(testResult)

        assertEquals(testResult, resultHandler.consumeResult())
    }
}
