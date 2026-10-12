package com.testing.miniproject2.ui.theme

import androidx.compose.ui.graphics.Color
import com.testing.miniproject2.interfaces.TareaEstado

fun TareaEstado.texto() = when (this) {
    TareaEstado.NO_EMPEZADA -> "No empezada"
    TareaEstado.EN_CURSO -> "En curso"
    TareaEstado.COMPLETADA -> "Completada"
    TareaEstado.VENCIDA->"Vencida"
}

fun TareaEstado.color() = when (this) {
    TareaEstado.NO_EMPEZADA -> Color(0xFFE53935)
    TareaEstado.EN_CURSO -> Color(0xFFFB8C00)
    TareaEstado.COMPLETADA -> Color(0xFF43A047)
    TareaEstado.VENCIDA->Color(0xFF691210)
}