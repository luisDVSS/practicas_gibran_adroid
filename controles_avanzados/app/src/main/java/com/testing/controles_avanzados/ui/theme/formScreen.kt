package com.testing.controles_avanzados.ui.theme

import android.R.attr.horizontalDivider
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.CustomCheckbox
import components.CustomDatePicker
import components.CustomRadiuButton
import components.CustomSpinner
import components.CustomSwitch

@Composable
fun FormScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Practica 4: Componentes Avanzados",
            fontSize = 22.sp,
            style = MaterialTheme.typography.headlineMedium
        )
        horizontalDivider
        Text("1. Switch", style = MaterialTheme.typography.titleMedium)
        CustomSwitch()
        horizontalDivider
        Text("2. RadioButton", style = MaterialTheme.typography.titleMedium)
        CustomRadiuButton()
        horizontalDivider
        Text("3. CheckBox", style = MaterialTheme.typography.titleMedium)
        CustomCheckbox()
        horizontalDivider
        Text("4. Spinner", style = MaterialTheme.typography.titleMedium)
        CustomSpinner()
        horizontalDivider
        Text("5. DatePicker", style = MaterialTheme.typography.titleMedium)
        CustomDatePicker()
        horizontalDivider
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick ={
            Toast.makeText(context,"Formulario completo",
                Toast.LENGTH_SHORT).show()
        },
            modifier=Modifier.fillMaxWidth()
            ) {
            Text("Probar formulario")
        }
    }

}