package com.example.appmaskcycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appmaskcycle.clases.DispMasc

class DetallesDispActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_disp)

        val extras = intent.extras!!
        val id = extras.getInt("id")
        val nombre = extras.getString("nombre")
        val tipo = extras.getString("tipo")
        val tInfo = extras.getString("tInfo")
        val lavados = extras.getInt("lavados")
        val duracion = extras.getInt("duracion")
        val stock = extras.getInt("stock")
        val comentario = extras.getString("comentario")

        val mascarilla = DispMasc(id,nombre!!,tipo!!,tInfo!!,lavados,duracion,stock,comentario!!)


    }
}