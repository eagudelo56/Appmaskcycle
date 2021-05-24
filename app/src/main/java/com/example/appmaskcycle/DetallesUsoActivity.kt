package com.example.appmaskcycle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appmaskcycle.api.DataCodigoError
import com.example.appmaskcycle.clases.UsoMasc
import com.example.appmaskcycle.util.ConvertirDb
import kotlinx.android.synthetic.main.activity_detalles_uso.*
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

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



        if(!mascarilla.activa){
            labelFin.visibility= View.GONE
            tvDetalleUsoFin.visibility= View.GONE
        }

        tvDetalleUsoNombre.text = mascarilla.nombre
        tvDetalleUsoTipo.text = mascarilla.tipo
        tvDetalleUsoTinfo.text = mascarilla.tInfo
        tvDetalleUsoHorasVida.text = mascarilla.getHoraVformato()
        tvDetalleUsoFin.text = mascarilla.getFinFormato()
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
                            quitarAlarma(mascarilla)
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

    private fun quitarAlarma(mascarilla:UsoMasc){
        val intentAlarm = Intent(AlarmClock.ACTION_DISMISS_ALARM)
        val fin = mascarilla.final
        val adelanto = -10
        fin.add(Calendar.MINUTE, adelanto)

        val horas = fin.get(Calendar.HOUR_OF_DAY)
        val min = fin.get(Calendar.MINUTE)

        intentAlarm.putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE, AlarmClock.ALARM_SEARCH_MODE_TIME)
        intentAlarm.putExtra(AlarmClock.EXTRA_HOUR,horas)
        intentAlarm.putExtra(AlarmClock.EXTRA_MINUTES,min)
        startActivity(intentAlarm)
    }

}