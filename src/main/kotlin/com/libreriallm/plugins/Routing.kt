package com.libreriallm.plugins

import com.libreriallm.rutas.rutasLibro
import com.libreriallm.rutas.rutasUsuario
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        // Importante llamar a las rutas personalizadas desde aquí.
        rutasLibro()
        rutasUsuario()
        // Static plugin. Try to access `/static/index.html`
        static {
            resources("static")
        }
    }
}
