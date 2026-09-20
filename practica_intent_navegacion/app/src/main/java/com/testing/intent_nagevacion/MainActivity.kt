package com.testing.intent_nagevacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.testing.intent_nagevacion.ui.theme.Intent_nagevacionTheme
import com.testing.intent_nagevacion.ui.theme.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Intent_nagevacionTheme {
                MainScreen()
            }
        }
    }
}
