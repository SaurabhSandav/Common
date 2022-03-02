package com.saurabhsandav.base

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.saurabhsandav.base.screen.common.theme.AppTheme
import com.saurabhsandav.common.compose.navigation.Navigator
import com.saurabhsandav.common.compose.saveable.serializableSaver

@Composable
internal fun App() {

    AppTheme(darkTheme = true) {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background,
            content = { AppNavigator() }
        )
    }
}

@Composable
private fun AppNavigator() {

    val routeGenerator = remember {
        var counter = 0
        generateSequence { "Route#${counter++}" }.iterator()
    }

    Navigator(routeGenerator.next(), routeSaver = serializableSaver()) { currentRoute, _ ->

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {

            Text(
                text = "Route Key - $currentRoute",
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {

                Button(onClick = { push(routeGenerator.next()) }) {
                    Text("Push")
                }

                Button(
                    enabled = canPop,
                    onClick = { pop() },
                ) {
                    Text("Pop")
                }
            }
        }
    }
}
