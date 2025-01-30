package com.example.verfuturo

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActividadVerFuturo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actividad_ver_futuro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Mapa para las imágenes del Zodiaco
        val recursosSigno = mapOf(
            "aries" to R.drawable.aries,
            "tauro" to R.drawable.tauro,
            "geminis" to R.drawable.geminis,
            "cancer" to R.drawable.cancer,
            "leo" to R.drawable.leo,
            "virgo" to R.drawable.virgo,
            "libra" to R.drawable.libra,
            "escorpio" to R.drawable.scorpio,
            "sagitario" to R.drawable.sagitario,
            "capricornio" to R.drawable.capricornio,
            "acuario" to R.drawable.acuario,
            "piscis" to R.drawable.piscis,
            )
        // Obtenemos los elementos gráficos
        val imgSigno = findViewById<ImageView>(R.id.imgFuturo)
        val txtPrediccion = findViewById<TextView>(R.id.txtFuturo)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        // Cuando se hace clic en el botón "Volver", llama a finish()
        btnVolver.setOnClickListener {
            finish()
        }

        // Obtener el nombre, día y el mes de nacimiento del Intent
        val nombre = intent.getStringExtra("nombre") ?: "Nombre"
        val diaNacimiento = intent.getIntExtra("dia", -1)
        val mesNacimiento = intent.getIntExtra("mes", -1)

        //Creamos un objeto de la clase Oráculo y la predicción
        val oraculo = Oraculo(diaNacimiento, mesNacimiento, this) // Pasamos el contexto de la actividad como argumento con this
        oraculo.obtenerPrediccion(nombre) { prediccion ->
            // Mostrar la predicción en la UI y la imagen correcta al ImageView
            txtPrediccion.text = prediccion
            val signoId = recursosSigno[oraculo.signo] ?: 0
            val nuevoDrawable = getDrawable(signoId)
            imgSigno.setImageDrawable(nuevoDrawable)
        }
    }
}


