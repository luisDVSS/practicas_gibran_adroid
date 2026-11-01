package com.example.miniproy3.data

import kotlinx.coroutines.flow.Flow

class PublicacionRepository(
    private val dao: PublicacionDao
) {

    val publicaciones: Flow<List<Publicacion>> =
        dao.obtenerTodas()

    suspend fun insertar(publicacion: Publicacion) {
        dao.insertar(publicacion)
    }

    suspend fun eliminar(id: Int) {
        dao.eliminar(id)
    }
}