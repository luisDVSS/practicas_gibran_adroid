package com.testing.sqllite_room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var nombre: String = ""
)