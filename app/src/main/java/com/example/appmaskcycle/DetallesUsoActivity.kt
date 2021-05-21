package com.example.appmaskcycle

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appmaskcycle.api.DataCodigoError
import com.example.appmaskcycle.clases.DispMasc
import com.example.appmaskcycle.clases.UsoMasc
import com.example.appmaskcycle.util.ConvertirDb
import kotlinx.android.synthetic.main.activity_detalles_uso.*
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetallesUsoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_uso)

        title = getString(R.string.nav_detalles)

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

        btnDetalleEliminar.setOnClickListener{
            elimUso(mascarilla)
        }
    }

    private fun elimUso(mascarilla: UsoMasc){
        val con: Context = this
        doAsync {
            val llamada = mascarilla.deleteUsoMasc(mascarilla.id)
            llamada.enqueue(object: Callback<DataCodigoError> {
                override fun onFailure(call: Call<DataCodigoError>, t: Throwable) {
                    Toast.makeText(con, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(
                    call: Call<DataCodigoError>,
                    response: Response<DataCodigoError>
                ) {
                    val respuesta = response.body()
                    if(respuesta!=null){
                        val codigo = respuesta.codigoError
                        if(codigo==1){
                            finish()
                            Toast.makeText(con, "Mascarilla eliminada correctamente", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(con, "Error al eliminar mascarilla", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            })

        }
    }
}