package com.libreriallm.datos.modelo

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Usuario(
    @BsonId
    var id: String = ObjectId().toString(),
    val nombre: String,
    val email: String
)
