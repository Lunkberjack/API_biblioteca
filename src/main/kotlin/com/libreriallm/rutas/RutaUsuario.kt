package com.libreriallm.rutas

import com.libreriallm.datos.crearActualizarDesdeId
import com.libreriallm.datos.crearActualizarUsuarioDesdeId
import com.libreriallm.datos.encontrarTodos
import com.libreriallm.datos.encontrarTodosUsuarios
import com.libreriallm.datos.modelo.Libro
import com.libreriallm.datos.modelo.Usuario
import com.libreriallm.datos.requests.Respuesta
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.rutasUsuario() {
    route("/usuario") {
        get("/todos") {
            val lista: List<Usuario> = encontrarTodosUsuarios()
            call.respond(
                HttpStatusCode.OK,
                lista
            )
        }

        post {
            val request = try {
                call.receive<Usuario>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            if (crearActualizarUsuarioDesdeId(request)) {
                call.respond(
                    HttpStatusCode.OK,
                    Respuesta(true, "Usuario creado (o actualizado) con éxito.", Unit)
                )
            } else {
                call.respond(HttpStatusCode.Conflict)
            }
        }
    }
}