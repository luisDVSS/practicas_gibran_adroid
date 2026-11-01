package com.example.usoapirest.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
// Función auxiliar para mapear los tipos de Pokémon a colores de cartas TCG
fun obtenerColorPorTipo(tipo: String): Color {
    return when (tipo.lowercase()) {
        "grass", "bug" -> Color(0xFF78C850)
        "fire" -> Color(0xFFFF8030)
        "water", "ice" -> Color(0xFF6890F0)
        "electric" -> Color(0xFFFFD700)
        "psychic", "poison", "ghost" -> Color(0xFFAA40A0)
        "fighting", "rock", "ground" -> Color(0xFFC03028)
        "fairy" -> Color(0xFFFEE99A)
        "dark" -> Color(0xFF705848)
        "steel" -> Color(0xFFB8B8D0)
        "dragon" -> Color(0xFF7038F8)
        else -> Color(0xFFFA8A78)
    }
}

@Composable
fun PokemonDetailScreen(
    viewModel: PokeViewModel,
    pokeId: String,
) {
    LaunchedEffect(pokeId) {
        viewModel.cargarDetallePokemon(pokeId)
    }

    val pokemon = viewModel.pokemonDetalle.value

    if (pokemon == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Obtenemos el tipo principal del Pokémon
        val tipoPrincipal =
            pokemon.types.firstOrNull()?.type?.name ?: "normal"
        val colorCarta = obtenerColorPorTipo(tipoPrincipal)

        // Fondo de la pantalla
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            // Diseñamos la carta TCG dinámica
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f),
                colors = CardDefaults.cardColors(
                    containerColor = colorCarta
                ),
                elevation = CardDefaults.cardElevation(16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Encabezado
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            pokemon.name.uppercase(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black
                        )

                        val hp = pokemon.stats.find { (_, stat) ->
                            stat.name == "hp"
                        }?.base_stat ?: 0

                        Text(
                            "HP $hp",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Imagen oficial
                    val imageUrl =
                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .border(
                                6.dp,
                                Color(0xFFC0C0C0),
                                RoundedCornerShape(4.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = pokemon.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.Black, thickness = 2.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Estadísticas básicas
                    Text(
                        "ESTADÍSTICAS (Ataques)",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    for ((baseStat, stat) in pokemon.stats.filter { it.stat.name != "hp" }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                stat.name.uppercase(),
                                color = Color.DarkGray,
                                fontWeight = FontWeight.Medium,
                            )

                            Text(
                                baseStat.toString(),
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black,
                            )
                        }
                    }
                }
            }
        }
    }
}
