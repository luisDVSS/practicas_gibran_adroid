package com.example.miniproy3.ui.theme


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.miniproy3.data.PublicacionRepository

class PublicacionViewModelFactory(
    private val repository: PublicacionRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(
                PublicacionViewModel::class.java
            )
        ) {
            return PublicacionViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido"
        )
    }
}