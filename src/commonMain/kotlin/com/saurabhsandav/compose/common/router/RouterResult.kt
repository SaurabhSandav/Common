package com.saurabhsandav.compose.common.router

public class RouterResultHandler {

    private var result: RouteResult = RouteResult.NoResult

    public fun consumeResult(): RouteResult = result.also { result = RouteResult.NoResult }

    public fun setResult(result: RouteResult) {
        this.result = result
    }
}

public interface RouteResult {

    public object NoResult : RouteResult
}
