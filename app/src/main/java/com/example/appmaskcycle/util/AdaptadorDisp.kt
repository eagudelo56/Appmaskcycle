package com.example.appmaskcycle.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.appmaskcycle.DetallesDispActivity
import com.example.appmaskcycle.DetallesUsoActivity
import com.example.appmaskcycle.HomeActivity
import com.example.appmaskcycle.R
import com.example.appmaskcycle.api.DataCodigoError
import com.example.appmaskcycle.api.DataUsoMasc
import com.example.appmaskcycle.clases.DispMasc
import com.example.appmaskcycle.clases.FactoriaDispMasc
import com.example.appmaskcycle.clases.FactoriaUsoMasc
import kotlinx.android.synthetic.main.disp_masc.view.*
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class AdaptadorDisp(private var content:Context, private var array:ArrayList<DispMasc>): RecyclerView.Adapter<AdaptadorDisp.ViewHolder>()
{
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(mascarilla: DispMasc, cont : Context) {
            //guarda el objeto de la poscion actual
            itemView.tvUsoNombre.text = mascarilla.nombre
            itemView.tvUsoDuracion.text = mascarilla.stock.toString()

            itemView.btnUsoAccion.setOnClickListener{
                if (mascarilla.stock > 0) {
                    val auxHorasVida = Calendar.getInstance()
                    auxHorasVida.set(Calendar.HOUR_OF_DAY, mascarilla.duracion)
                    auxHorasVida.set(Calendar.MINUTE, 0)
                    auxHorasVida.set(Calendar.SECOND, 0)

                    val auxFinal = Calendar.getInstance()
                    auxFinal.add(Calendar.HOUR_OF_DAY, mascarilla.duracion)

                    val idPack = mascarilla.id
                    val inicio = ConvertirDb.getStringFromCalendar(Calendar.getInstance())
                    val activa = ConvertirDb.getStringFromBoolean(true)
                    val horasVida = ConvertirDb.getStringFromCalendar(auxHorasVida)
                    val final = ConvertirDb.getStringFromCalendar(auxFinal)
                    val lavados = mascarilla.lavados
                    insertarUsoDb(
                        cont,
                        idPack,
                        inicio,
                        activa,
                        horasVida,
                        final,
                        lavados,
                        mascarilla
                    )
                }
            }

            //*pasar los datos a verDetallesDisp*/
            itemView.btnUsoVerDetalles.setOnClickListener {
                val intent = Intent(cont, DetallesDispActivity::class.java)
                intent.putExtra("id",mascarilla.id)
                intent.putExtra("nombre", mascarilla.nombre)
                intent.putExtra("tipo", mascarilla.tipo)
                intent.putExtra("tInfo",mascarilla.tInfo)
                intent.putExtra("lavados",mascarilla.lavados)
                intent.putExtra("duracion",mascarilla.duracion)
                intent.putExtra("stock", mascarilla.stock)
                intent.putExtra("comentario",mascarilla.comentario)
                cont.startActivity(intent)
            }
        }

        private fun updateStock(cont : Context,mascarilla: DispMasc) {
            if (mascarilla.stock > 0){
                mascarilla.stock -=1
                actualizarDip(cont, mascarilla)
            }
        }

        private fun actualizarDip (cont : Context, mascarilla: DispMasc) {
            doAsync {
                val llamada = mascarilla.updateDispMasc(mascarilla.id,
                    mascarilla.nombre, mascarilla.lavados,
                    mascarilla.duracion,mascarilla.stock, mascarilla.comentario)
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
                                    cont.startActivity(Intent(cont,HomeActivity::class.java))
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


        private fun insertarUsoDb(cont : Context, idPack: Int, inicio: String, activa: String,
                          horasVida: String, final: String, lavados: Int, mascarilla : DispMasc){
            doAsync {
                val objDao = FactoriaUsoMasc.getUsoMascDao()
                val llamada = objDao.insertarUsoMasc(idPack,
                    inicio,activa,horasVida,final,lavados)
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
                                    updateStock(cont,mascarilla)
                                    Toast.makeText(cont,"bien", Toast.LENGTH_LONG).show()
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


    override fun getItemCount(): Int {
        return array.size
    }

    //por cada fila encontrada en la bd, asigna el layout de disp_masc
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.disp_masc, parent, false))
    }

    //por cada fila encontrada en la bd, rellena la informacion del arrayList
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = array[position]
        holder.bind(item, content)
    }


}