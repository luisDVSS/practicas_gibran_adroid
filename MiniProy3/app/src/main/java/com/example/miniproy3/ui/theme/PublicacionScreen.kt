package com.example.miniproy3.ui.theme



import android.graphics.Bitmap
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.miniproy3.data.Publicacion
import java.io.File
import java.io.FileOutputStream

private fun guardarFoto(
    context: Context,
    bitmap: Bitmap
): String? {

    return try {
        val archivo = File(
            context.filesDir,
            "foto_${System.currentTimeMillis()}.jpg"
        )

        FileOutputStream(archivo).use { output ->
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                90,
                output
            )
        }

        Uri.fromFile(archivo).toString()

    } catch (e: Exception) {
        null
    }
}

@Composable
fun PublicacionScreen(
    viewModel: PublicacionViewModel
) {

    val context = LocalContext.current

    val publicaciones by viewModel.publicaciones
        .collectAsState()

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fotoUri by remember { mutableStateOf<String?>(null) }
    var fotoBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val fechaActual = remember {
        java.text.SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            java.util.Locale.getDefault()
        ).format(java.util.Date())
    }

    // Seleccionar imagen de galería
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->

        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {
                // Algunos proveedores no permiten persistir el permiso.
            }

            fotoUri = it.toString()
            fotoBitmap = null
        }
    }

    // Tomar fotografía
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->

        bitmap?.let {
            fotoBitmap = it
            fotoUri = guardarFoto(context, it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Mis publicaciones",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Fecha: $fechaActual",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = {
                    camaraLauncher.launch(null)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Cámara")
            }

            OutlinedButton(
                onClick = {
                    galeriaLauncher.launch(arrayOf("image/*"))
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Galería")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Vista previa de la fotografía
        if (fotoBitmap != null) {

            Image(
                bitmap = fotoBitmap!!.asImageBitmap(),
                contentDescription = "Fotografía seleccionada",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )

        } else if (fotoUri != null) {

            AsyncImage(
                model = fotoUri,
                contentDescription = "Imagen seleccionada",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {

                if (titulo.isNotBlank() &&
                    descripcion.isNotBlank()
                ) {

                    viewModel.guardarPublicacion(
                        titulo = titulo,
                        descripcion = descripcion,
                        fotoUri = fotoUri
                    )

                    titulo = ""
                    descripcion = ""
                    fotoUri = null
                    fotoBitmap = null
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar publicación")
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = "Publicaciones guardadas",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (publicaciones.isEmpty()) {

            Text("Todavía no hay publicaciones.")

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = publicaciones,
                    key = { it.id }
                ) { publicacion ->

                    PublicacionItem(
                        publicacion = publicacion,
                        onEliminar = {
                            viewModel.eliminarPublicacion(
                                publicacion.id
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PublicacionItem(
    publicacion: Publicacion,
    onEliminar: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            if (publicacion.fotoUri != null) {

                AsyncImage(
                    model = publicacion.fotoUri,
                    contentDescription = "Foto de ${publicacion.titulo}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = publicacion.titulo,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = publicacion.descripcion,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Fecha: ${publicacion.fecha}",
                style = MaterialTheme.typography.labelSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                TextButton(
                    onClick = onEliminar
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}
