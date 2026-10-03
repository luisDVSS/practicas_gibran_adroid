package com.testing.registro_estudiantes.ui.theme

import android.app.Activity
import androidx.compose.material.icons.Icons
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class PantallaConfirmacion : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val nombre =
            intent.getStringExtra("EX_NOMBRE")?.takeIf { it.isNotBlank() } ?: "Sin nombre"
        val matricula =
            intent.getStringExtra("EX_MATRICULA")?.takeIf { it.isNotBlank() } ?: "Sin nombre"
        val carrera =
            intent.getStringExtra("EX_CARRERA")?.takeIf { it.isNotBlank() } ?: "Sin nombre"
        val estado = intent.getBooleanExtra("EX_ESTADO", false)
        setContent {
            ScreenConfirmacion(
                nombre = nombre,
                matricula = matricula,
                carrera = carrera,
                estado = estado
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenConfirmacion(nombre: String, matricula: String, carrera: String, estado: Boolean) {
    val context = LocalContext.current
    val preferencias = context.getSharedPreferences("data", Context.MODE_PRIVATE)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmacion") },
                navigationIcon = {
                    IconButton(onClick = {
                        (context as Activity).finish()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Resumen de registro",
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 22.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    //vertical padding o margin verticalmente por cada elemento
                    //es como el padding-top en css
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Nombre: $nombre"
                    )
                    Text(
                        text = "Matricula: $matricula"
                    )
                    Text(
                        text = "Carrera: $carrera"
                    )
                    Text(
                        text = if (estado) {
                            "Estado: Activo"
                        } else {
                            "Estado: Inactivo"
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    try {
                        preferencias.edit().putString("KEY_NOMBRE", nombre)
                            .putString("KEY_MATRICULA", matricula)
                            .putString("KEY_CARRERA", carrera)
                            .putBoolean("KEY_ACTIVO", estado)
                            .apply()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Ocurrio un error: $e", Toast.LENGTH_SHORT).show()
                    }
                    Toast.makeText(context, "Datos guardados", Toast.LENGTH_SHORT).show()

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }

}