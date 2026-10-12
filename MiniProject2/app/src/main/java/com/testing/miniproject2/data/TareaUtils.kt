package com.testing.miniproject2.data

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.testing.miniproject2.interfaces.TareaEstado
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs

enum class Orden(val texto: String) {
    PREDETERMINADO("Predeterminado"),
    COMPLETADAS("Completadas primero"),
    NO_EMPEZADAS("No empezadas primero"),
    EN_CURSO("En curso primero"),
    VENCIDAS("Vencidas primero"),
    FECHA("Más próximas a vencer")


}

fun List<Tarea>.ordenarPor(orden: Orden): List<Tarea> = when (orden) {
    Orden.PREDETERMINADO -> sortedBy { it.id }
    Orden.COMPLETADAS -> sortedWith(
        compareBy<Tarea> { it.estado != TareaEstado.COMPLETADA }.thenBy { it.fecha_fin }
    )
    Orden.NO_EMPEZADAS-> sortedWith(
        compareBy<Tarea> { it.estado != TareaEstado.NO_EMPEZADA}.thenBy { it.fecha_fin }
    )
    Orden.EN_CURSO -> sortedWith(
        compareBy<Tarea> { it.estado != TareaEstado.EN_CURSO }.thenBy { it.fecha_fin }
    )

    Orden.VENCIDAS -> sortedWith(
        compareBy<Tarea> { it.estado != TareaEstado.VENCIDA }.thenBy { it.fecha_fin }
    )
    // Pendientes por fecha más cercana, luego vencidas, y al final las completadas
    Orden.FECHA -> sortedWith(
        compareBy<Tarea> {
            when (it.estado) {
                TareaEstado.COMPLETADA -> 2
                TareaEstado.VENCIDA -> 1
                else -> 0
            }
        }.thenBy { it.fecha_fin }
    )
}

/** Texto con mínimo horas: "2 d 5 h", "5 h", "menos de 1 h" */
fun textoTiempo(fechaFin: Long, ahora: Long): String {
    val diff = fechaFin - ahora
    val horasTotales = abs(diff) / 3_600_000L
    val dias = horasTotales / 24
    val horas = horasTotales % 24

    val cantidad = when {
        horasTotales < 1L -> "menos de 1 h"
        dias == 0L -> "$horas h"
        horas == 0L -> "$dias d"
        else -> "$dias d $horas h"
    }
    return if (diff >= 0) "Tiempo restante: $cantidad" else "Venció hace $cantidad"
}

fun formatearFecha(millis: Long): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(millis))

// --- Conversión entre el DatePicker (UTC) y la hora local ---

fun aMillisUtc(fechaLocal: Long): Long {
    val local = Calendar.getInstance().apply { timeInMillis = fechaLocal }
    return Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        clear()
        set(local.get(Calendar.YEAR), local.get(Calendar.MONTH), local.get(Calendar.DAY_OF_MONTH))
    }.timeInMillis
}

fun combinarFechaHora(fechaUtc: Long, hora: Int, minuto: Int): Long {
    val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = fechaUtc }
    return Calendar.getInstance().apply {
        clear()
        set(
            utc.get(Calendar.YEAR),
            utc.get(Calendar.MONTH),
            utc.get(Calendar.DAY_OF_MONTH),
            hora,
            minuto,
            0
        )
    }.timeInMillis
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorFechaHora(
    fechaFin: Long,
    onFechaChange: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var mostrarFecha by remember { mutableStateOf(false) }
    var mostrarHora by remember { mutableStateOf(false) }
    var fechaElegidaUtc by remember { mutableStateOf<Long?>(null) }

    OutlinedButton(
        onClick = { mostrarFecha = true },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text("Entrega: ${formatearFecha(fechaFin)}")
    }

    if (mostrarFecha) {
        val dateState = rememberDatePickerState(initialSelectedDateMillis = aMillisUtc(fechaFin))
        DatePickerDialog(
            onDismissRequest = { mostrarFecha = false },
            confirmButton = {
                TextButton(onClick = {
                    fechaElegidaUtc = dateState.selectedDateMillis
                    mostrarFecha = false
                    if (fechaElegidaUtc != null) mostrarHora = true
                }) { Text("Siguiente") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarFecha = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = dateState)
        }
    }

    if (mostrarHora) {
        val cal = Calendar.getInstance().apply { timeInMillis = fechaFin }
        val timeState = rememberTimePickerState(
            initialHour = cal.get(Calendar.HOUR_OF_DAY),
            initialMinute = cal.get(Calendar.MINUTE),
            is24Hour = true
        )
        AlertDialog(
            onDismissRequest = { mostrarHora = false },
            title = { Text("Hora de entrega") },
            text = { TimePicker(state = timeState) },
            confirmButton = {
                TextButton(onClick = {
                    fechaElegidaUtc?.let {
                        onFechaChange(combinarFechaHora(it, timeState.hour, timeState.minute))
                    }
                    mostrarHora = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarHora = false }) { Text("Cancelar") }
            }
        )
    }
}