package com.libreriallm.datos.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Libro(
    val titulo: String,
    val autor: String,
    val sinopsis: String,
    val genero: String,
    val portada: String
)
