package com.libreriallm.rutas

import com.libreriallm.datos.*
import com.libreriallm.datos.modelo.Libro
import com.libreriallm.datos.requests.LibroRequest
import com.libreriallm.datos.requests.Respuesta
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.rutasLibro() {
    route("/libro") {
        get {
            val libroId = call.receive<LibroRequest>().id
            val libro = libroDesdeId(libroId)
            libro?.let {
                call.respond(
                    HttpStatusCode.OK,
                    Respuesta(true, "El libro se ha encontrado", it)
                )
            } ?: call.respond(
                HttpStatusCode.OK,
                Respuesta(true, "No hay ningún libro con ese id", Unit)
            )
        }
        /**
         * Devolver todos los libros en la base de datos.
         */
        get("/todos") {
            val lista: List<Libro>? = encontrarTodos()
            lista?.let {
                call.respond(
                    HttpStatusCode.OK,
                    lista // Devuelve la lista completa
                )
            } ?: call.respond(
                HttpStatusCode.OK,
                Respuesta(true, "No hay ningún libro con ese id", Unit)
            )
        }
        /**
         * Crear un libro o actualizarlo si ya existe.
         */
        post {
            // Se recibe un libro en formato JSON desde Postman.
           val request = try {
               call.receive<Libro>()
           } catch (e: ContentTransformationException) {
               call.respond(HttpStatusCode.BadRequest)
               return@post
           }

           if (crearActualizarDesdeId(request)) {
               call.respond(
                   HttpStatusCode.OK,
                   Respuesta(true, "Libro creado (o actualizado) con éxito.", Unit)
               )
           } else {
               call.respond(HttpStatusCode.Conflict)
           }
        }

        /**
         * Put hace automáticamente lo mismo que nosotros hemos conseguido
         * en el create-update: se asegura de que el recurso exista antes de
         * decidir si lo actualizará o creará.
         */
        put {
            val request = try {
                call.receive<Libro>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }

            if (actualizarDesdeId(request)) {
                call.respond(
                    HttpStatusCode.OK,
                    Respuesta(true, "Libro creado (o actualizado) con éxito.", Unit)
                )
            } else {
                call.respond(HttpStatusCode.Conflict)
            }
        }

        delete {
            val libroId = call.receive<LibroRequest>().id
            if (borrarDesdeId(libroId)) {
                call.respond(
                    HttpStatusCode.OK,
                    Respuesta(true, "Libro eliminado con éxito.", Unit)
                )
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}