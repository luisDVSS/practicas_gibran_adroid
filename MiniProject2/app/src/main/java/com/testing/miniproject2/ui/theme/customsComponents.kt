package com.testing.miniproject2.ui.theme

import android.widget.Space
import android.widget.Switch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.testing.miniproject2.interfaces.TareaEstado
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.testing.miniproject2.data.Tarea
import com.testing.miniproject2.data.textoTiempo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorEstado(
    estado: TareaEstado,
    onEstadoChange: (TareaEstado) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandido by remember { mutableStateOf(false) }

    Box(modifier) {
        Surface(
            onClick = { expandido = true },
            shape = CircleShape,
            color = estado.color().copy(alpha = 0.15f),
            contentColor = estado.color()
        ) {
            Row (
                modifier = Modifier.padding(start = 12.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .size(8.dp)
                        .background(estado.color(), CircleShape)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = estado.texto(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false },
            shape = RoundedCornerShape(16.dp)
        ) {

            TareaEstado.entries.filter{ it != TareaEstado.VENCIDA }.forEach { opcion ->
                DropdownMenuItem(
                    leadingIcon = {
                        Box(
                            Modifier
                                .size(10.dp)
                                .background(opcion.color(), CircleShape)
                        )
                    },
                    text = { Text(opcion.texto()) },
                    onClick = {
                        onEstadoChange(opcion)
                        expandido = false
                    }
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaCard(
    tarea: Tarea,
    ahora: Long,
    onClick: () -> Unit,
    onEstadoChange: (TareaEstado) -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.height(IntrinsicSize.Min)) {
            Box(
                Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(tarea.estado.color())
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = tarea.tarea,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (tarea.descripcion.isNotBlank()) {
                    Text(
                        text = tarea.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Tiempo restante (no se muestra en tareas completadas)
                if (tarea.estado != TareaEstado.COMPLETADA) {
                    val vencida = tarea.estado == TareaEstado.VENCIDA
                    val colorTiempo = if (vencida) tarea.estado.color()
                    else MaterialTheme.colorScheme.onSurfaceVariant
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            tint = colorTiempo,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = textoTiempo(tarea.fecha_fin, ahora),
                            style = MaterialTheme.typography.labelMedium,
                            color = colorTiempo
                        )
                    }
                }

                SelectorEstado(
                    estado = tarea.estado,
                    onEstadoChange = onEstadoChange
                )
            }
        }
    }
}