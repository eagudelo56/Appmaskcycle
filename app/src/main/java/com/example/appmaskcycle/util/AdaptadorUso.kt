package com.example.appmaskcycle.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.provider.AlarmClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.appmaskcycle.DetallesUsoActivity
import com.example.appmaskcycle.HomeActivity
import com.example.appmaskcycle.R
import com.example.appmaskcycle.api.DataCodigoError
import com.example.appmaskcycle.clases.FactoriaUsoMasc
import com.example.appmaskcycle.clases.UsoMasc
import kotlinx.android.synthetic.main.uso_masc.view.*
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class AdaptadorUso (private var content:Context, private var array:ArrayList<UsoMasc>): RecyclerView.Adapter<AdaptadorUso.ViewHolder>(){

    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        private val colorAmarillo = Color.parseColor("#999900")
        private val colorNormal = Color.parseColor("#03DAC5")
        private val colorLavar = Color.parseColor("#003d99")

        private fun cambiarBtnPausa(){
            itemView.btnUsoAccion.setBackgroundColor(colorAmarillo)
            itemView.btnUsoAccion.setImageResource(R.drawable.ic_pause)
        }

        private fun cambiarBtnUsar(){
            itemView.btnUsoAccion.setBackgroundColor(colorNormal)
            itemView.btnUsoAccion.setImageResource(R.drawable.ic_usar)
        }
        private fun cambiarBtnLavar(){
            itemView.btnUsoAccion.setBackgroundColor(colorLavar)
            itemView.btnUsoAccion.setImageResource(R.drawable.ic_water)
        }

        fun bind(mascarilla:UsoMasc,content: Context){
            //mascarilla guarda el objeto de la poscion actual
            itemView.tvUsoNombre.text = mascarilla.nombre
            itemView.tvUsoDuracion.text = mascarilla.getHoraVformato()

            if(mascarilla.activa){
                cambiarBtnPausa()
            }else{
                cambiarBtnUsar()
            }

            itemView.btnUsoAccion.setOnClickListener{
                if(mascarilla.activa){
                    cambiarBtnUsar()
                    pausarMascarilla(content,mascarilla)
                }else{
                    cambiarBtnPausa()
                    activarMascarilla(content,mascarilla)
                }
            }

            /* boton ver detalles*/
            itemView.btnUsoVerDetalles.setOnClickListener {
                val intent = Intent(content,DetallesUsoActivity::class.java)
                intent.putExtra("id",mascarilla.id)
                intent.putExtra("nombre", mascarilla.nombre)
                intent.putExtra("tipo", mascarilla.tipo)
                intent.putExtra("tInfo",mascarilla.tInfo)
                intent.putExtra("inicio",ConvertirDb.getStringFromCalendar(mascarilla.inicio))
                intent.putExtra("activa", mascarilla.activa)
                intent.putExtra("horasVida",ConvertirDb.getStringFromCalendar(mascarilla.horasVida))
                intent.putExtra("final", ConvertirDb.getStringFromCalendar(mascarilla.final))
                intent.putExtra("lavados",mascarilla.lavados)
                content.startActivity(intent)
            }
        }

        private fun eliminarUso(id: Int,cont:Context) {
            doAsync {
                val objDao = FactoriaUsoMasc.getUsoMascDao()
                val llamada = objDao.deleteUsoMasc(id)
                llamada.enqueue(
                    object : Callback<DataCodigoError>{
                        override fun onFailure(call: Call<DataCodigoError>, t: Throwable) {
                            Toast.makeText(cont,t.localizedMessage, Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(
                            call: Call<DataCodigoError>,
                            response: Response<DataCodigoError>
                        ) {
                            val respuesta = response.body()
                            if(respuesta!=null){
                                val codigo = respuesta.codigoError
                                if(codigo == 1){
                                    Toast.makeText(cont,"bien Eli", Toast.LENGTH_LONG).show()
                                }else{
                                    Toast.makeText(cont,"mal", Toast.LENGTH_LONG).show()
                                }
                            }
                        }

                    }
                )
            }
        }

        private fun actualizarUso (cont : Context, id:Int, inicio: String,
                                   activa: String,
                                   horasVida: String,
                                   final: String,
                                   lavados: Int) {
            doAsync {
                val objDao = FactoriaUsoMasc.getUsoMascDao()
                val llamada = objDao.updateUsoMasc(id, inicio,
                    activa, horasVida,
                    final, lavados)
                llamada.enqueue(
                    object : Callback<DataCodigoError> {
                        override fun onFailure(call: Call<DataCodigoError>, t: Throwable) {
                            Toast.makeText(cont,t.localizedMessage, Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(
                            call: Call<DataCodigoError>,
                            response: Response<DataCodigoError>
                        ) {
                            val respuesta = response.body()
                            if(respuesta!=null){
                                val codigo = respuesta.codigoError
                                if(codigo == 1){
                                    Toast.makeText(cont,"bien actua", Toast.LENGTH_LONG).show()
                                    /*
                                    * se termina el home activity y se vuelve a lanzar
                                    * para refrescar los datos
                                    *
                                    * overridePendingTransition es para que
                                    * no se vea la animacion del cambio de activity
                                    *
                                    * */
                                    (cont as Activity).finish()
                                    cont .overridePendingTransition(0, 0)
                                    cont.startActivity(Intent(cont, HomeActivity::class.java))
                                    cont .overridePendingTransition(0, 0)
                                }else{
                                    Toast.makeText(cont,"mal", Toast.LENGTH_LONG).show()
                                }
                            }
                        }

                    }
                )
            }

        }


        private fun quitarAlarma(mascarilla:UsoMasc,content: Context){
            val intentAlarm = Intent(AlarmClock.ACTION_DISMISS_ALARM)

            intentAlarm.putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE, AlarmClock.ALARM_SEARCH_MODE_LABEL)
            intentAlarm.putExtra(AlarmClock.EXTRA_MESSAGE,"${mascarilla.nombre}_${mascarilla.id}")
            content.startActivity(intentAlarm)
        }

        private fun ponerAlarma(cont: Context,mascarilla: UsoMasc){
            val fin = mascarilla.final
            val adelanto = -10
            fin.add(Calendar.MINUTE, adelanto)

            val horas = fin.get(Calendar.HOUR_OF_DAY)
            val min = fin.get(Calendar.MINUTE)

            val intentAlarm = Intent(AlarmClock.ACTION_SET_ALARM)
            intentAlarm.putExtra(AlarmClock.EXTRA_MESSAGE,
                "${mascarilla.nombre}_${mascarilla.id}")
            intentAlarm.putExtra(AlarmClock.EXTRA_HOUR,horas)
            intentAlarm.putExtra(AlarmClock.EXTRA_MINUTES,min)
            intentAlarm.putExtra(
                AlarmClock.EXTRA_DAYS,
                ArrayList<Int>(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)))
            intentAlarm.putExtra(AlarmClock.EXTRA_SKIP_UI,true)
            cont.startActivity(intentAlarm)
        }


        private fun pausarMascarilla(cont: Context,mascarilla: UsoMasc){
            /*pausa - inicio*/
            val actualCal = Calendar.getInstance()
            actualCal.set(Calendar.ZONE_OFFSET,0)
            val actual = actualCal.timeInMillis
            if(actual<mascarilla.final.timeInMillis){
                mascarilla.activa = false
                quitarAlarma(mascarilla,cont)

                val final = mascarilla.final.timeInMillis
                var diferencia = final.minus(actual)
                //diferencia = diferencia.minus(3600000.toLong())

                //diferencia = diferencia.plus(600000.toLong())
                mascarilla.horasVida.timeInMillis = diferencia
                actualizarUso(
                    cont,
                    mascarilla.id,
                    ConvertirDb.getStringFromCalendar(mascarilla.inicio),
                    ConvertirDb.getStringFromBoolean(mascarilla.activa),
                    ConvertirDb.getStringFromCalendar(mascarilla.horasVida),
                    ConvertirDb.getStringFromCalendar(mascarilla.final),
                    mascarilla.lavados
                )
            }else{
                eliminarUso(mascarilla.id, cont)
            }
        }

        private fun activarMascarilla(cont: Context,mascarilla: UsoMasc){
            mascarilla.activa = true
            val inicio = Calendar.getInstance()

            val final = Calendar.getInstance()

            val horasVida = mascarilla.horasVida.get(Calendar.HOUR_OF_DAY)
            val minutosVida = mascarilla.horasVida.get(Calendar.MINUTE)
            final.add(Calendar.HOUR_OF_DAY,horasVida)
            final.add(Calendar.MINUTE,minutosVida)
            ponerAlarma(cont,mascarilla)
            actualizarUso(
                cont,
                mascarilla.id,
                ConvertirDb.getStringFromCalendar(inicio),
                ConvertirDb.getStringFromBoolean(mascarilla.activa),
                ConvertirDb.getStringFromCalendar(mascarilla.horasVida),
                ConvertirDb.getStringFromCalendar(final),
                mascarilla.lavados
            )
        }

    }

    override fun getItemCount():Int{
        return array.size
    }


    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):ViewHolder{
        val layoutInflater=LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.uso_masc,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = array[position]
        holder.bind(item,content)

    }
}
