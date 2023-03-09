package com.libreriallm.datos

import com.libreriallm.datos.modelo.Libro
import com.libreriallm.datos.modelo.Usuario
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("BibliotecaLLM")

private val coleccionLibros = database.getCollection<Libro>()
private val coleccionUsuarios = database.getCollection<Usuario>()

suspend fun encontrarTodosUsuarios(): List<Usuario> {
    return coleccionUsuarios.find().toList()
}

suspend fun crearActualizarUsuarioDesdeId(usuario: Usuario): Boolean {
    val existe = coleccionUsuarios.findOneById(usuario.id) != null
    return if (existe) {
        coleccionUsuarios.updateOneById(usuario.id, usuario).wasAcknowledged()
    } else {
        usuario.id = ObjectId().toString()
        coleccionUsuarios.insertOne(usuario).wasAcknowledged()
    }
}

/**
 * Devuelve la colección completa de libros.
 */
suspend fun encontrarTodos(): List<Libro> {
    return coleccionLibros.find().toList()
}

/**
 * Devuelve un libro cuyo id sea igual al que se ha pasado.
 */
suspend fun libroDesdeId(id: String): Libro? {
        return coleccionLibros.findOneById(id)
}

/**
 * Si el libro existe, lo actualiza con los nuevos datos.
 * Si no existe, se añade.
 */
suspend fun crearActualizarDesdeId(libro: Libro): Boolean {
    val existe = coleccionLibros.findOneById(libro.id) != null
    return if (existe) {
        coleccionLibros.updateOneById(libro.id, libro).wasAcknowledged()
    } else {
        libro.id = ObjectId().toString()
        coleccionLibros.insertOne(libro).wasAcknowledged()
    }
}

/**
 * Actualiza un libro con los nuevos datos.
 * No es necesario implementar el método otra vez, pero lo hago
 * para demostrar los cuatro métodos de las API REST.
 */
suspend fun actualizarDesdeId(libro: Libro): Boolean {
    // Se pasan el id del libro a modificar y los nuevos datos.
    return coleccionLibros.updateOneById(libro.id, libro).wasAcknowledged()
}

/**
 * Elimina un libro del cual se pasa el id.
 */
suspend fun borrarDesdeId(libroId: String): Boolean {
    val libro = coleccionLibros.findOne(Libro::id eq libroId)
    libro?.let { lib ->
        return coleccionLibros.deleteOneById(lib.id).wasAcknowledged()
    } ?: return false
}