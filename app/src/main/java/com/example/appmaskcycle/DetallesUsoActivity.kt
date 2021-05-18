package com.example.appmaskcycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appmaskcycle.clases.UsoMasc
import com.example.appmaskcycle.util.ConvertirDb
import kotlinx.android.synthetic.main.activity_detalles_uso.*

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


        tvDetalleUsoNombre.text = mascarilla.nombre
        tvDetalleUsoTipo.text = mascarilla.tipo
        tvDetalleUsoTinfo.text = mascarilla.tInfo
        tvDetalleUsoHorasVida.text = mascarilla.getHoraVformato()
        tvDetalleUsoLavados.text = mascarilla.lavados.toString()

    }
}