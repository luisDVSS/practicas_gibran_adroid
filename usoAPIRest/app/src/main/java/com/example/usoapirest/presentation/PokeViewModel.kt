package com.example.usoapirest.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.usoapirest.data.NamedResource
import com.example.usoapirest.data.PokemonDetailResponse
import com.example.usoapirest.data.PokeRepository
import kotlinx.coroutines.launch

class PokeViewModel : ViewModel() {

    private val repository = PokeRepository()

    var tipos = mutableStateOf<List<NamedResource>>(emptyList())
    var pokemonsPorTipo = mutableStateOf<List<NamedResource>>(emptyList())
    var pokemonDetalle = mutableStateOf<PokemonDetailResponse?>(null)

    init {
        // Cargar las categorías apenas se abra la app
        viewModelScope.launch {
            try {
                tipos.value = repository.getTypes()
            } catch (_: Exception) {
            }
        }
    }

    fun cargarPokemonsPorTipo(id: String) {
        viewModelScope.launch {
            try {
                pokemonsPorTipo.value = repository.getPokemonsByType(id).map { (pokemon) -> pokemon }
            } catch (_: Exception) {
            }
        }
    }

    fun cargarDetallePokemon(id: String) {
        pokemonDetalle.value = null // Limpiamos la vista anterior

        viewModelScope.launch {
            try {
                pokemonDetalle.value = repository.getPokemonDetail(id)
            } catch (_: Exception) {
            }
        }
    }
}
