package com.saurabhsandav.base

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.saurabhsandav.base.screen.common.theme.AppTheme
import com.saurabhsandav.common.compose.navigation.NavHost
import com.saurabhsandav.common.compose.navigation.rememberNavController
import com.saurabhsandav.common.compose.saveable.serializableSaver
import com.saurabhsandav.common.core.navigation.pop
import com.saurabhsandav.common.core.navigation.push

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

    val controller = rememberNavController(0, serializableSaver())

    NavHost(controller) { currentRoute, _ ->

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {

            Text(
                text = "Route#${currentRoute.key}",
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {

                Button(onClick = { controller.push(currentRoute.key + 1) }) {
                    Text("Push")
                }

                Button(
                    onClick = { controller.pop() },
                    enabled = controller.canPop,
                ) {
                    Text("Pop")
                }
            }
        }
    }
}
