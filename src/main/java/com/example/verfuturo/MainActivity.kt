 package com.example.verfuturo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val boton = findViewById<Button>(R.id.btnEnviar)
        val texto = findViewById<EditText>(R.id.txtNombre)
        val calendario = findViewById<DatePicker>(R.id.datePicker)

        boton.setOnClickListener {
            val dia = calendario.dayOfMonth
            val mes = calendario.month+1 // Los meses en DatePicker comienzan desde 0
            val anio = calendario.year
            val nombre = texto.text.toString()

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa tu nombre.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this,"$nombre fecha seleccionada: $dia/$mes/$anio", Toast.LENGTH_SHORT).show()

            // Crear instancia de Oraculo
            val oraculo = Oraculo(dia, mes, this)


            // Obtener predicción
            oraculo.obtenerPrediccion(nombre) { prediccion ->
                runOnUiThread {
                    // Para realizar el envío de información de una activity a la otra
                    val intentFuturo = Intent(this, ActividadVerFuturo::class.java)
                    // Añadir el nombre, el día y el mes de nacimiento como extras
                    intentFuturo.putExtra("nombre", nombre)
                    intentFuturo.putExtra("dia", dia)
                    intentFuturo.putExtra("mes", mes)
                    intentFuturo.putExtra("prediccion", prediccion)
                    startActivity(intentFuturo)
                }
            }

        }
    }
}