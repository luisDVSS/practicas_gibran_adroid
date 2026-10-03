package com.testing.rviewdinamiclist.ui.theme

data class  Contacto(
    val id:Int,
    val nombre: String,
    val telefono: String
)
fun obtenerContactosDummy(): List<Contacto>{
    return listOf(
        Contacto(1,"Ana Garcia", "555-1001"),
        Contacto(2,"Luis Perez", "555-1002"),
        Contacto(3,"Maria Lopez", "555-1003"),
        Contacto(4,"Carlos Ramirez", "555-1004"),
        Contacto(5,"Sofia Torres", "555-1005"),
        Contacto(6,"Jorge Medina", "555-1006"),
        Contacto(7,"Laura Salazar", "555-1007"),
        Contacto(8,"Pedro Castillo", "555-1008"),
        Contacto(9,"Elena Rojas", "555-1009"),
        Contacto(10,"Diego Castillo", "555-1010"),
        Contacto(11,"Vaeleria Silva", "555-1011"),
        Contacto(12,"Ricardo Soto", "555-1012")
    )
}