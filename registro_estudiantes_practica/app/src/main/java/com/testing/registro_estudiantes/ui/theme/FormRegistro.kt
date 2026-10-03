package com.testing.registro_estudiantes.ui.theme

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.testing.registro_estudiantes.componetes.EstadoSwitch
import com.testing.registro_estudiantes.componetes.SelectCarrera

@Composable
fun FormRegistro() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
//    if(sharedPreferences.contains("EX_NOMBRE")){
//        Toast.makeText(context, "Ya se ha registrado", Toast.LENGTH_SHORT).show()
//    }
    //dato : mutableStateof retorno un string no nulleable, diferente a un string nullable
    var matricula by remember {
        mutableStateOf(
            sharedPreferences.getString("KEY_MATRICULA", "") ?: ""
        )
    }
    var nombre by remember {
        mutableStateOf(
            sharedPreferences.getString("KEY_NOMBRE", "") ?: ""
        )
    }
    var carrera by remember { mutableStateOf(sharedPreferences.getString("KEY_CARRERA", "") ?: "") }
    var isActivo by remember { mutableStateOf(sharedPreferences.getBoolean("KEY_ACTIVO", false)) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Practica: Registro de Alumnos",
            fontSize = 22.sp,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre Completo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = matricula,
            onValueChange = { matricula = it },
            label = { Text("Matricula") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(18.dp))

        SelectCarrera(
            selectedText = carrera, onClickChange = {
                carrera = it
            })

        EstadoSwitch(
            checked = isActivo, onCheckedChange = { isActivo = it })
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier.fillMaxWidth(), onClick = {
                val intent = Intent(context, PantallaConfirmacion::class.java).apply {
                    putExtra("EX_NOMBRE", nombre)
                    putExtra("EX_MATRICULA", matricula)
                    putExtra("EX_CARRERA", carrera)
                    putExtra("EX_ESTADO", isActivo)
                }
                context.startActivity(intent)
            }) {
            Text(text = "Siguiente")
        }
    }

}