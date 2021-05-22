package com.example.appmaskcycle

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appmaskcycle.api.DataCodigoError
import com.example.appmaskcycle.clases.DispMasc
import kotlinx.android.synthetic.main.activity_detalles_disp.*
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetallesDispActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_disp)


        title = getString(R.string.nav_detalles)

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

        tvDetalleDispNombre.text = mascarilla.nombre
        tvDetalleDispTipo.text = mascarilla.tipo
        tvDetalleDispDuracion.text = mascarilla.duracion.toString()
        tvDetalleDispStock.text = mascarilla.stock.toString()
        tvDetalleDispLavados.text = mascarilla.lavados.toString()
        tvDetalleDispComentario.text = mascarilla.comentario


        btnDetalleDispEliminar.setOnClickListener{
            elimDisp(mascarilla)
        }


    }

    private fun elimDisp(mascarilla:DispMasc){
        val con:Context = this
        doAsync {
            val llamada = mascarilla.deleteDispMasc(mascarilla.id)
            llamada.enqueue(object:Callback<DataCodigoError>{
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