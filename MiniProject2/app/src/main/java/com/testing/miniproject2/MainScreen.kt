package com.testing.miniproject2

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.More
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.testing.miniproject2.data.AppDatabase
import com.testing.miniproject2.data.Orden
import com.testing.miniproject2.data.Tarea
import com.testing.miniproject2.data.ordenarPor
import com.testing.miniproject2.interfaces.TareaEstado
import com.testing.miniproject2.ui.theme.AddTaskDialog
import com.testing.miniproject2.ui.theme.ShowDetails
import com.testing.miniproject2.ui.theme.SelectorEstado
import com.testing.miniproject2.ui.theme.TareaCard
import com.testing.miniproject2.ui.theme.TareaSwipeable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    var mostrarDialog by remember { mutableStateOf(false) }
    var tareaSeleccionada by remember { mutableStateOf<Tarea?>(null) }
    var menuExpandido by remember { mutableStateOf(false) }
    var orden by remember { mutableStateOf(Orden.PREDETERMINADO) }
    var ahora by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var idsOcultos by remember { mutableStateOf(setOf<Int>()) }
    var versiones by remember { mutableStateOf(mapOf<Int, Int>()) }

    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val dao = db.tareaDao()
    val coroutineScope = rememberCoroutineScope()

    // Preferencia guardada: ocultar completadas
    val prefs = remember { context.getSharedPreferences("ajustes", Context.MODE_PRIVATE) }
    var ocultarCompletas by remember {
        mutableStateOf(prefs.getBoolean("ocultar_completas", false))
    }

    val tareas by dao.getAllTasks().collectAsState(initial = emptyList())
    val tareasOrdenadas = remember(tareas, orden) { tareas.ordenarPor(orden) }
    val tareasVisibles = remember(tareasOrdenadas, idsOcultos, ocultarCompletas) {
        tareasOrdenadas.filter {
            it.id !in idsOcultos &&
                    !(ocultarCompletas && it.estado == TareaEstado.COMPLETADA)
        }
    }

    // Cada minuto: refresca el tiempo mostrado y marca las vencidas
    LaunchedEffect(Unit) {
        while (true) {
            ahora = System.currentTimeMillis()
            withContext(Dispatchers.IO) {
                dao.marcarVencidas(TareaEstado.VENCIDA, TareaEstado.COMPLETADA, ahora)
            }
            delay(60_000)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis tareas", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Clic en navegacion", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { menuExpandido = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                    }
                    DropdownMenu(
                        expanded = menuExpandido,
                        onDismissRequest = { menuExpandido = false },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Acerca de") },
                            onClick = {
                                menuExpandido = false
                                Toast.makeText(
                                    context,
                                    "Acerca de seleccionada",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                        HorizontalDivider()

                        // Ocultar completadas (con palomita)
                        DropdownMenuItem(
                            text = { Text("Ocultar completadas") },
                            leadingIcon = {
                                if (ocultarCompletas) {
                                    Icon(Icons.Default.Check, contentDescription = null)
                                } else {
                                    Spacer(Modifier.size(24.dp))
                                }
                            },
                            onClick = {
                                ocultarCompletas = !ocultarCompletas
                                prefs.edit()
                                    .putBoolean("ocultar_completas", ocultarCompletas)
                                    .apply()
                                menuExpandido = false
                            }
                        )
                        HorizontalDivider()

                        Text(
                            "Ordenar por",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                        Orden.entries.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion.texto) },
                                leadingIcon = {
                                    if (orden == opcion) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    } else {
                                        Spacer(Modifier.size(24.dp))
                                    }
                                },
                                onClick = {
                                    orden = opcion
                                    menuExpandido = false
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { mostrarDialog = true },
                shape = RoundedCornerShape(20.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nueva tarea") }
            )
        }
    ) { innerPadding ->

        if (mostrarDialog) {
            AddTaskDialog(onDismiss = { mostrarDialog = false })
        }

        tareaSeleccionada?.let { tarea ->
            ShowDetails(
                tarea = tarea,
                onDismiss = { tareaSeleccionada = null },
                onGuardar = { nuevoTitulo, nuevaDescripcion, nuevaFecha ->
                    // Si estaba vencida y le pusieron una fecha futura, vuelve a "No empezada"
                    val nuevoEstado =
                        if (tarea.estado == TareaEstado.VENCIDA && nuevaFecha > System.currentTimeMillis())
                            TareaEstado.NO_EMPEZADA
                        else tarea.estado

                    coroutineScope.launch(Dispatchers.IO) {
                        dao.updateTarea(
                            tarea = nuevoTitulo,
                            descripcion = nuevaDescripcion,
                            estado = nuevoEstado,
                            fechaFin = nuevaFecha,
                            id = tarea.id
                        )
                    }
                    tareaSeleccionada = null
                }
            )
        }

        if (tareasVisibles.isEmpty()) {
            val soloOcultas = tareas.isNotEmpty() && ocultarCompletas

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    if (soloOcultas) "No hay tareas pendientes" else "Aún no tienes tareas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    if (soloOcultas) "Tus tareas completadas están ocultas"
                    else "Toca \"Nueva tarea\" para crear la primera",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 96.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tareasVisibles, key = { "${it.id}-${versiones[it.id] ?: 0}" }) { tarea ->
                    TareaSwipeable(
                        tarea = tarea,
                        ahora = ahora,
                        onClick = { tareaSeleccionada = tarea },
                        onEstadoChange = { nuevoEstado ->
                            coroutineScope.launch(Dispatchers.IO) {
                                dao.changeTaskState(nuevoEstado, tarea.id)
                            }
                        },
                        onEliminar = {
                            // Se oculta de la lista, pero sigue en la BD
                            idsOcultos = idsOcultos + tarea.id
                            coroutineScope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                val resultado = snackbarHostState.showSnackbar(
                                    message = "Tarea eliminada",
                                    actionLabel = "Deshacer",
                                    duration = SnackbarDuration.Short
                                )
                                if (resultado == SnackbarResult.ActionPerformed) {
                                    // Deshacer: reaparece con key nueva (estado de swipe limpio)
                                    versiones =
                                        versiones + (tarea.id to ((versiones[tarea.id] ?: 0) + 1))
                                    idsOcultos = idsOcultos - tarea.id
                                } else {
                                    withContext(Dispatchers.IO) { dao.deleteTask(tarea.id) }
                                    idsOcultos = idsOcultos - tarea.id
                                }
                            }
                        },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}