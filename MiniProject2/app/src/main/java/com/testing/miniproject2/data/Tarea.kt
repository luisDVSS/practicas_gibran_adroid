package com.testing.miniproject2.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.testing.miniproject2.interfaces.TareaEstado

@Entity(tableName = "tareas")
data class Tarea(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var tarea: String,
    var descripcion:String,
    var estado: TareaEstado,
    var fecha_fin:Long
)