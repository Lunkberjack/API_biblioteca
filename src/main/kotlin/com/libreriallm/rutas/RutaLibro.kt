package com.libreriallm.rutas

import com.libreriallm.datos.modelo.Libro
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// Si lo testeamos en el móvil, poner la IP local del ordenador en vez
// de localhost.
private const val URL_BASE = "http:://localhost:8100"

private val libros = listOf(
    Libro("Canción de Hielo y Fuego I: Juego de Tronos", "George R.R. Martin", "El primer título de la famosa saga de fantasía" +
            " oscura de George R.R. Martin.", "Fantasía oscura/épica", "$URL_BASE/libros/1.jpeg"),
    Libro("Hyperion", "Dan Simmons", "Un diplomático, un sacerdote, un militar, un poeta, un profesor, una detective y un navegante" +
            " entrecruzan sus vidas y sus destinos en su peregrinar en busca del Alcaudón y de las Tumbas de Tiempo, majestuosas e incomprensibles " +
            "construcciones que albergan un secreto procedente del futuro. Sus historias personales componen una sugerente visión caleidoscópica de la " +
            "compleja sociedad en la que viven y a la que, tal vez, puedan salvar.", "Ciencia ficción", "$URL_BASE/libros/2.jpg"),
    Libro("La mano izquierda de la oscuridad", "Ursula K. Le Guin", "Los guedenianos tienen una particularidad que los hace únicos: " +
            "son hermafroditas, y adoptan uno u otro sexo exclusivamente en la época de celo, denominada kémmer. En Invierno, Ai contacta con Estraven, un " +
            "alto cargo que le mostrará cuán diferente puede llegar a ser una sociedad donde no existe una diferenciación sexual.", "Ciencia ficción",
            "$URL_BASE/libros/3.jpg"),
    Libro("Así habló Zaratustra", "Friedrich Nietzsche", "mi  man", "Filosofía", "$URL_BASE/libros/4.jpg"),
    Libro("La estación de la calle Perdido", "China Miéville", "La metrópolis se extiende desde el centro del mundo. Humanos y razas arcanas " +
            "malviven en la penumbra bajo sus chimeneas, donde las fábricas y fundiciones amartillan la noche. Durante más de mil años, el Parlamento y su brutal " +
            "milicia han gobernado una vasta economía de obreros y artistas, espías y soldados, yonquis y prostitutas. Pero acaba de llegar un extraño con el bolsillo " +
            "lleno y una demanda imposible. ", "Fantasía/ciencia ficción/dios sabe", "$URL_BASE/libros/5.jpg"),
    )
fun Route.libroRandom() {
    get("/random") {
        call.respond(
            HttpStatusCode.OK,
            libros.random()
        )
    }
}