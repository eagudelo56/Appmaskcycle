package com.example.appmaskcycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.appmaskcycle.api.Conexion
import com.example.appmaskcycle.api.DataUsuarios
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        //Toast.makeText(this,"eeee",Toast.LENGTH_LONG).show()
        pruebaNom()

    }

    private  fun pruebaNom() {
        val cont =this

        doAsync {
            val objConexion = Conexion()
            val sql = "select * from usuarios"
            val llamada = /*en esta variable guardamos la llamada al archivo php*/
            objConexion.executeQueryUsuario(sql)

            llamada.enqueue( /*con este meto EJECUTAMOS la llamada*/
                object : Callback<List<DataUsuarios>>{ /*palabra clave del metodo*/
                    override fun onFailure(call: Call<List<DataUsuarios>>, t: Throwable) { /*ERROR*/
                        Toast.makeText(cont,t.localizedMessage,Toast.LENGTH_LONG).show()
                        Toast.makeText(cont," no funciona",Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse( /*esta bien*/
                        call: Call<List<DataUsuarios>>,
                        response: Response<List<DataUsuarios>>
                    ) {
                        //val aux = response.errorBody()
                        //Log.e("tag",aux.toString())

                        val aux = response.body()
                        val rel = response.body() as List<DataUsuarios>

                        //Toast.makeText(cont,rel[0].contrasena,Toast.LENGTH_LONG).show()
                        Toast.makeText(cont,rel[0].hello(),Toast.LENGTH_LONG).show()

                    }
                }
            )
        }

    }
}