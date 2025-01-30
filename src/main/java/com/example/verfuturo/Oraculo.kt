package com.example.verfuturo

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.common.util.concurrent.RateLimiter
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.util.Calendar


class Oraculo(val dia: Int, val mes: Int, private val context: Context) {

    // Creo un array con Signos del Zodiaco
    private val signosZodiaco = arrayOf(
        "aries", "tauro", "geminis", "cancer", "leo", "virgo",
        "libra", "escorpio", "sagitario", "capricornio", "acuario", "piscis"
    )

    // Atributo con el signo para poder acceder desde fuera
    val signo = getSignoZodiaco(dia, mes)

    // Método para obtener el signo del Zodiaco
    private fun getSignoZodiaco(dia: Int, mes: Int): String {
        var signo = ""
        when (mes) {
            1 -> signo = if (dia < 20) signosZodiaco[9] else signosZodiaco[10]
            2 -> signo = if (dia < 19) signosZodiaco[10] else signosZodiaco[11]
            3 -> signo = if (dia < 21) signosZodiaco[11] else signosZodiaco[0]
            4 -> signo = if (dia < 20) signosZodiaco[0] else signosZodiaco[1]
            5 -> signo = if (dia < 21) signosZodiaco[1] else signosZodiaco[2]
            6 -> signo = if (dia < 21) signosZodiaco[2] else signosZodiaco[3]
            7 -> signo = if (dia < 23) signosZodiaco[3] else signosZodiaco[4]
            8 -> signo = if (dia < 23) signosZodiaco[4] else signosZodiaco[5]
            9 -> signo = if (dia < 23) signosZodiaco[5] else signosZodiaco[6]
            10 -> signo = if (dia < 23) signosZodiaco[6] else signosZodiaco[7]
            11 -> signo = if (dia < 22) signosZodiaco[7] else signosZodiaco[8]
            12 -> signo = if (dia < 22) signosZodiaco[8] else signosZodiaco[9]
        }
        return signo
    }

    private fun obtenerDiaDeLaSemana(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_WEEK) - 1 // Restamos 1 para alinear con el array de días
    }

    // Crear un RateLimiter que permita 1 solicitud por segundo
    private val rateLimiter = RateLimiter.create(1.0)  // 1 solicitud por segundo

    // Método suspendido para realizar la solicitud a la API de Mistral
    private suspend fun obtenerFraseMistral(signo: String): String {
        return withContext(Dispatchers.IO) {
            // Esperar hasta que sea el momento adecuado para hacer una solicitud
            rateLimiter.acquire()  // Espera si el límite de tasa ha sido alcanzado
            try {
                // Crear el prompt basado en el signo del zodiaco
                val messages = listOf(
                    Message(role = "system", content = "Eres un oráculo que da predicciones únicas, hazlo como si fueses un horoscopo. Quiero una respuesta de no mas de 80 tokens."),
                    Message(role = "user", content = "Dame una predicción para el signo $signo.")
                )

                // Crear la solicitud a la API
                val request = ApiRequest(
                    model = "open-mistral-nemo",
                    messages = messages,
                    max_tokens = 100,
                    temperature = 0.7
                )

                // Realizar la llamada con Retrofit
                val response = RetrofitClient.apiInterface.obtenerPrediccion(
                    "Bearer ${Config.getApiKey(context)}", request
                ).execute()

                Thread.sleep(2000) // Pausa de 2 segundos entre solicitudes

                // Procesar la respuesta
                if (response.isSuccessful) {
                    response.body()?.choices?.firstOrNull()?.message?.content?.trim()
                        ?: "No se encontró predicción."
                } else {
                    "Error en la respuesta de la API: ${response.code()}"
                }
            } catch (e: HttpException) {
                Log.e("Oraculo", "Error HTTP: ${e.message}", e)
                "Error HTTP al obtener la predicción."
            } catch (e: Exception) {
                Log.e("Oraculo", "Error inesperado: ${e.message}", e)
                "Error al obtener la predicción: ${e.message}"
            }
        }
    }

    // Método público para obtener la predicción (con callback)
    fun obtenerPrediccion(nombre: String, callback: (String) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            // Obtener la predicción de la API
            val fraseMistral = obtenerFraseMistral(signo)

            // Preparar el mensaje final para mostrar
            val prediccion = "Hola $nombre, \n$fraseMistral"

            // Enviar el resultado al hilo principal
            Handler(Looper.getMainLooper()).post {
                callback(prediccion)
            }
        }
    }
}