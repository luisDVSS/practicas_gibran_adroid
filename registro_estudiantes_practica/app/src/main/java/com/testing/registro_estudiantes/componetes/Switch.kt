package com.testing.registro_estudiantes.componetes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun EstadoSwitch(

  checked:Boolean,
  onCheckedChange:(Boolean)->Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Estatus:")
        Text(text=if(checked){"Activo"}else{"Inactivo"})

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                uncheckedThumbColor =Color(0xFF731E1E),
                uncheckedTrackColor =Color(0xFFD23C3C),
                checkedThumbColor =Color(0xFF3D6B40),
                checkedTrackColor =Color(0xFF6EBA71)
            )
        )
    }
}