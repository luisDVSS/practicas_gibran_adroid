package com.testing.sqllite_room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao{
    @Query("SELECT * FROM usuario")
    fun obtenerTodos(): Flow<List<Usuario>>

    @Insert
    fun insertar(usuario : Usuario)

}