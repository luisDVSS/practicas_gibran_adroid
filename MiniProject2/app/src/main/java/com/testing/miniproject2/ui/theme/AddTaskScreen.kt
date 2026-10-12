package com.testing.miniproject2.ui.theme
import kotlin.collections.emptyList
import android.graphics.drawable.shapes.Shape
import android.graphics.fonts.FontStyle
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.testing.miniproject2.data.AppDatabase
import com.testing.miniproject2.data.SelectorFechaHora
import com.testing.miniproject2.data.Tarea
import com.testing.miniproject2.interfaces.TareaEstado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

@Composable
fun AddTaskDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val dao = db.tareaDao()
    val coroutineScope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    // Por defecto: dentro de 24 horas
    var fechaFin by remember { mutableLongStateOf(System.currentTimeMillis() + 24 * 3_600_000L) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Nueva tarea",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Título") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                SelectorFechaHora(
                    fechaFin = fechaFin,
                    onFechaChange = { fechaFin = it }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    Button(
                        shape = RoundedCornerShape(12.dp),
                        onClick = {
                            when {
                                nombre.isBlank() || descripcion.isBlank() ->
                                    Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()

                                fechaFin <= System.currentTimeMillis() ->
                                    Toast.makeText(context, "La fecha debe ser futura", Toast.LENGTH_SHORT).show()

                                else -> {
                                    coroutineScope.launch(Dispatchers.IO) {
                                        dao.createTask(
                                            Tarea(
                                                tarea = nombre.trim(),
                                                descripcion = descripcion.trim(),
                                                estado = TareaEstado.NO_EMPEZADA,
                                                fecha_fin = fechaFin
                                            )
                                        )
                                    }
                                    onDismiss()
                                }
                            }
                        }
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}