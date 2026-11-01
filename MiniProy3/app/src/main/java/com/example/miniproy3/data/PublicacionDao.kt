package com.example.miniproy3.data
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PublicacionDao {

    @Insert
    suspend fun insertar(publicacion: Publicacion)

    @Query("SELECT * FROM publicaciones ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<Publicacion>>

    @Query("DELETE FROM publicaciones WHERE id = :id")
    suspend fun eliminar(id: Int)
}