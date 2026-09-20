package com.testing.intent_nagevacion.ui.theme

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen(){
    val context = LocalContext.current



    var nombre by remember { mutableStateOf("")}
    var correo by remember { mutableStateOf("")}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement= Arrangement.Center
    ) {
        Text(
            text = "Practica 03: Intents y Navegacion",
            fontSize = 22.sp,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = {nombre=it},
            label = {Text("Nombre Completo")},
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = correo,
            onValueChange = {correo=it},
            label = {Text("Correo completo")},
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                val intent = Intent(context, ProfileActivity::class.java).apply{
                    putExtra("EXTRA_NOMBRE",nombre)
                    putExtra("EXTRA_CORREO",correo
                    )
                }
                    context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Ver Perfil (Intent Explicito")
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(onClick = {
            val sendIntent = Intent(Intent.ACTION_SEND).apply{
                putExtra(Intent.EXTRA_TEXT,"Hola, mi nombre es $nombre y mi correo es $correo")
                type = "text/plain"
            }
            val choser = Intent.createChooser(sendIntent,"Compartir datos")
            context.startActivity(choser)
        },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Compartir Datos (Intent Implicito)")
        }
    }
}