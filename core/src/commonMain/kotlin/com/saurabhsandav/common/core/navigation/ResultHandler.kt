package com.saurabhsandav.common.core.navigation

public class ResultHandler internal constructor() {

    private var result: RouteResult? = null

    internal fun setResult(result: RouteResult) {
        this.result = result
    }

    public fun consumeResult(): RouteResult? {
        return result.also { result = null }
    }
}

public interface RouteResult
