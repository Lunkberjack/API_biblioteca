package com.libreriallm.datos.requests

import kotlinx.serialization.Serializable

@Serializable
data class Respuesta<T> (
    val status: Boolean,
    val message: String,
    val data: T
)
