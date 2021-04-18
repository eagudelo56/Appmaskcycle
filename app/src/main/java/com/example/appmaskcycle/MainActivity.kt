package com.example.appmaskcycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.appmaskcycle.api.Conexion
import com.example.appmaskcycle.api.DataUsuarios
import com.example.appmaskcycle.clases.FactoriaUsuarios
import com.example.appmaskcycle.clases.Usuarios
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Toast.makeText(this,"eeee",Toast.LENGTH_LONG).show()
        bnLogin.setOnClickListener {
            comprobarFormulario()
        }

    }


    private fun comprobarFormulario () {
        if (etUser.text.toString().isNotEmpty() && etContra.text.toString().isNotEmpty()){
            comprobarLogin(etUser.text.toString(),etContra.text.toString())
        }else{
            Toast.makeText(this,"Datos incompletos",Toast.LENGTH_LONG).show()
        }
    }
    private  fun comprobarLogin(usuario:String,contrasena:String) {
        val cont =this

        doAsync {
            val objDAO = FactoriaUsuarios.getUsuarioDao()
            val llamada = objDAO.getUsuarioPorNombre(usuario)
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

                        val array = Usuarios.convertir(rel)

                        if(contrasena == array[0].contrasena){
                            val intent = Intent(cont, HomeActivity::class.java)
                            startActivity(intent)
                        }

                        //Toast.makeText(cont,array[0].nombre,Toast.LENGTH_LONG).show()
                        //Toast.makeText(cont,rel[0].hello(),Toast.LENGTH_LONG).show()

                    }
                }
            )
        }

    }
}