package com.example.appmaskcycle.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
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

class AdaptadorUso (var content:Context,var array:ArrayList<UsoMasc>): RecyclerView.Adapter<AdaptadorUso.ViewHolder>(){

    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        fun bind(mascarilla:UsoMasc){
            //guarda el objeto de la poscion actual
            itemView.tvUsoNombre.text = mascarilla.nombre
            itemView.tvUsoDuracion.text =
                mascarilla.getHoraVformato()

            itemView.btnUsoAccion.setOnClickListener{
                if(mascarilla.activa){
                    mascarilla.activa = false
                    val actual = Calendar.getInstance().timeInMillis
                    val inicio = mascarilla.inicio.timeInMillis
                    val diferencia = actual.minus(inicio)
                    val newFecha = Calendar.getInstance()
                    newFecha.time = Date(diferencia)
                }
            }
        }

        private fun actualizarUso (cont : Context, inicio: String,
                                   activa: String,
                                   horasVida: String,
                                   final: String,
                                   lavados: Int) {
            doAsync {
                val objDao = FactoriaUsoMasc.getUsoMascDao()
                val llamada = objDao.updateUsoMasc(inicio,
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
                                    Toast.makeText(cont,"bien", Toast.LENGTH_LONG).show()
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
        holder.bind(item)
    }
}
