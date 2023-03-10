package com.libreriallm.datos.modelo

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Libro(
    @BsonId
    var id: String = ObjectId().toString(),
    val titulo: String,
    val autor: String,
    val sinopsis: String,
    val genero: String,
)
