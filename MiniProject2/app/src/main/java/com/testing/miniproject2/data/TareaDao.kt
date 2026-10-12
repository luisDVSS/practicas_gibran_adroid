package com.testing.miniproject2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.testing.miniproject2.interfaces.TareaEstado
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
 @Query("SELECT * FROM tareas")
 fun getAllTasks(): Flow<List<Tarea>>

 @Query("UPDATE tareas SET tarea=:tarea, descripcion=:descripcion, estado=:estado, fecha_fin=:fechaFin WHERE id=:id")
 fun updateTarea(tarea: String, descripcion: String, estado: TareaEstado, fechaFin: Long, id: Int): Int

 @Query("SELECT * FROM tareas where id=:id")
 fun getTaskById(id: Int): Tarea?

 @Insert
 fun createTask(tarea: Tarea)

 @Query("DELETE FROM tareas WHERE id=:id")
 fun deleteTask(id: Int): Int

 @Query("UPDATE tareas SET estado=:estado_nuevo WHERE id=:id")
 fun changeTaskState(estado_nuevo: TareaEstado, id: Int): Int

 // Marca como vencidas las tareas que no están completadas y ya pasaron su fecha
 @Query("UPDATE tareas SET estado=:vencida WHERE estado != :completada AND estado != :vencida AND fecha_fin < :ahora")
 fun marcarVencidas(vencida: TareaEstado, completada: TareaEstado, ahora: Long): Int
}