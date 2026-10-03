package com.testing.registro_estudiantes.componetes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text

@Composable
fun SelectCarrera(
    selectedText:String,
    onClickChange:(String)-> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
        )

        DropdownMenu (
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Ing Software") },
                onClick = {
                    onClickChange("Ing Software")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Ing Geodesia") },
                onClick = {
                    onClickChange("Ing Geodesia")
                    expanded = false

                }
            )
            DropdownMenuItem(
                text = { Text("Ing PI") },
                onClick = {
                    onClickChange("Ing PI")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Ing Nano") },
                onClick = {
                    onClickChange("Ing Nano")
                    expanded = false
                }
            )
        }
    }
}