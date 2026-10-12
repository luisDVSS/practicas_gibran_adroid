package com.testing.sqllite_room

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.emptyList

@Composable
fun MainScreen(){
    val context = LocalContext.current

    val db = remember { AppDatabase.getDatabase(context) }
    val dao = db.usuarioDao()
    val coroutineScope = rememberCoroutineScope()

    val usuarios by dao.obtenerTodos().collectAsState(initial = emptyList())
    var nuevoNombre by remember { mutableStateOf("") }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .padding(top=32.dp)
    ) {
        OutlinedTextField(
            value = nuevoNombre,
            onValueChange = {nuevoNombre= it},
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier=Modifier.height(8.dp))
        Button(
            onClick={
                if(nuevoNombre.isNotBlank()){
                    coroutineScope.launch(Dispatchers.IO){
                        dao.insertar(Usuario(nombre=nuevoNombre))
                        withContext(Dispatchers.Main){
                            nuevoNombre=""
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Guardar Usuario")
        }
        Spacer(modifier=Modifier.height(16.dp))
        Text("Lista de usuarios registrados",style= MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(usuarios){usuario->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text="ID: ${usuario.id} | Nombre:  ${usuario.nombre}",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}