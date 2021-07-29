package com.saurabhsandav.compose.common.navigator

internal class ResultHandler {

    private var result: RouteResult? = null

    fun setResult(result: RouteResult) {
        this.result = result
    }

    fun consumeResult(): RouteResult? {
        return result.also { result = null }
    }
}

public interface RouteResult
