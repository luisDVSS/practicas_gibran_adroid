package com.example.miniproy3.ui.theme



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miniproy3.data.Publicacion
import com.example.miniproy3.data.PublicacionRepository

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PublicacionViewModel(
    private val repository: PublicacionRepository
) : ViewModel() {

    val publicaciones = repository.publicaciones.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun guardarPublicacion(
        titulo: String,
        descripcion: String,
        fotoUri: String?
    ) {

        if (titulo.isBlank() || descripcion.isBlank()) {
            return
        }

        val publicacion = Publicacion(
            titulo = titulo.trim(),
            descripcion = descripcion.trim(),
            fotoUri = fotoUri,
            fecha = java.text.SimpleDateFormat(
                "dd/MM/yyyy HH:mm",
                java.util.Locale.getDefault()
            ).format(java.util.Date())
        )

        viewModelScope.launch {
            repository.insertar(publicacion)
        }
    }

    fun eliminarPublicacion(id: Int) {
        viewModelScope.launch {
            repository.eliminar(id)
        }
    }
}