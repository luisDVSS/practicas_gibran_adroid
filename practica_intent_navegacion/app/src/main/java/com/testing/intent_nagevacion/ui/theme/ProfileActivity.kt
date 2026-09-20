package com.testing.intent_nagevacion.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val nombre = intent.getStringExtra("EXTRA_NOMBRE")?.takeIf { it.isNotBlank() } ?: "Sin Nombre"
        val correo = intent.getStringExtra("EXTRA_CORREO")?.takeIf { it.isNotBlank() } ?: "Sin correo"
        setContent {
            ProfileScreen(
                nombre = nombre,
                correo = correo,
                onBackClick = { finish() }
            )
        }
    }
}

