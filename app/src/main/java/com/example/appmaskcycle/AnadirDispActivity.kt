package com.example.appmaskcycle

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.appmaskcycle.api.DataCodigoError
import com.example.appmaskcycle.clases.FactoriaDispMasc
import com.example.appmaskcycle.clases.Usuarios
import kotlinx.android.synthetic.main.activity_anadir_disp.*
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AnadirDispActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_disp)


        btnAnadir.setOnClickListener{
            validarFormulario()
        }

        val listaMascarillas = arrayOf("Quirugíca",
            "FFp2", "Higiénicas","Personalizadas","Reutilizables")

        val adaptadorSpin = ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item, listaMascarillas)
        spTipos.adapter = adaptadorSpin
    }

    private fun validarFormulario () {
        var relleno = true
        val usuario = Usuarios.idActual
        val arrayEt = arrayOf(etNombre,etStock,etDuracion)
        for ( i in arrayEt) {
            if(i.text.toString().isEmpty()){
                relleno=false
            }
        }
        if(relleno && usuario!=null){
            insertarBD(etNombre.text.toString(),1,8,8,
                etStock.text.toString().toInt(),usuario)
        }
    }



    private fun insertarBD (nombre:String, tipo:Int, lavados:Int, duracion:Int, stock:Int, usuario:Int) {
        val cont=this
            doAsync {
                val objetoDao = FactoriaDispMasc.getDispMascDao()
                val llamada =
                    objetoDao.insertarDispMasc(nombre,tipo,lavados,duracion
                        ,stock,"", usuario)
                llamada.enqueue(
                    object : Callback<DataCodigoError> {
                        override fun onFailure(call: Call<DataCodigoError>, t: Throwable) {
                            Toast.makeText(cont,"no conexion",Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(
                            call: Call<DataCodigoError>,
                            response: Response<DataCodigoError>
                        ) {
                            val respuesta = response.body()
                            if(respuesta!=null){
                                if(respuesta.codigoError==1){
                                    Toast.makeText(cont,"Mascarilla insertada",Toast.LENGTH_LONG).show()
                                    (cont as Activity).finish()/*acaba la activitdad del fomulario*/

                                }
                                else if(respuesta.codigoError==0){
                                    Toast.makeText(cont,"Mal "+respuesta.codigoError,Toast.LENGTH_LONG).show()
                                }
                                else{
                                    Toast.makeText(cont,"ni 0 ni 1",Toast.LENGTH_LONG).show()
                                }
                            }
                            else{
                                Toast.makeText(cont,"Servidor ha fallado",Toast.LENGTH_LONG).show()
                            }
                        }

                    }
                )
            }
    }


}