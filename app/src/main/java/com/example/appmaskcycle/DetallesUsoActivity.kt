package com.example.appmaskcycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appmaskcycle.clases.UsoMasc
import com.example.appmaskcycle.util.ConvertirDb
import java.util.*

class DetallesUsoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_uso)

        val extras = intent.extras!!
        val id = extras.getInt("id")
        val nombre = extras.getString("nombre")
        val tipo = extras.getString("tipo")
        val tInfo = extras.getString("tInfo")
        val inicio = extras.getString("inicio")
        val activa = extras.getBoolean("activa")
        val horasVida = extras.getString("horasVida")
        val final = extras.getString("final")
        val lavados = extras.getInt("lavados")


        val inicioCal = ConvertirDb.getCalendarFromString(inicio!!)
        val horasVidaCal = ConvertirDb.getCalendarFromString(horasVida!!)
        val finalCal = ConvertirDb.getCalendarFromString(final!!)
        val mascarilla = UsoMasc(id,nombre!!,tipo!!,tInfo!!,
            inicioCal,activa,horasVidaCal,finalCal,lavados)


        tvDetalleUsoNombre.setText(mascarilla.nombre)
        tvDetalleUsoTipo.setText(mascarilla.tipo)
        tvDetalleUsoTinfo.setText(mascarilla.tInfo)
        tvDetalleUsoHorasVida.setText(mascarilla.getHoraVformato())
        tvDetalleUsoLavados.setText(mascarilla.lavados.toString())

    }
}