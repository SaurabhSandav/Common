package com.saurabhsandav.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.saurabhsandav.base.ComposeAndroidApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ComposeAndroidApp()
    }
}
