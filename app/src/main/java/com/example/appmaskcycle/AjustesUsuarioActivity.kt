package com.example.appmaskcycle

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.appmaskcycle.api.DataCodigoError
import com.example.appmaskcycle.api.DataUsuarios
import com.example.appmaskcycle.clases.FactoriaUsuarios
import com.example.appmaskcycle.clases.Usuarios
import kotlinx.android.synthetic.main.activity_ajustes_usuario.*
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AjustesUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes_usuario)

        title = getString(R.string.nav_ajustes_usr)
        btnGuardar.setOnClickListener{
            validarForm()
        }
    }

    private fun validarForm(){

        val passOld = etPassOld.text.toString()
        val passNew1 = etPassNew1.text.toString()
        val passNew2 = etPassNew2.text.toString()
        if(passOld.isNotEmpty() && passNew1.isNotEmpty() && passNew2.isNotEmpty()){
            if(passOld.length>=4 && passNew1.length >=4 && passNew2.length>=4){
                if(passNew1==passNew2){
                    val id = Usuarios.idActual
                    if(id!=null){
                        comprobarContraOld(passOld,passNew1,id)
                    }
                }else{
                    Toast.makeText(this,"La contraseñas nuevas no coinciden", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this,"La contraseña debe tener 4 caracteres como minimo", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this,"Datos incompletos", Toast.LENGTH_SHORT).show()
        }

    }

    private fun updateUser(usr:Int,nombre:String,pass:String){
        val cont = this
        doAsync {
            val objDao = FactoriaUsuarios.getUsuarioDao()
            val llamada = objDao.updateUsuario(usr,nombre,pass)
            llamada.enqueue(
                object : Callback<DataCodigoError>{
                    override fun onFailure(call: Call<DataCodigoError>, t: Throwable) {
                        Toast.makeText(cont,t.localizedMessage,Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<DataCodigoError>,
                        response: Response<DataCodigoError>
                    ) {
                        val resp = response.body()
                        if(resp!=null){
                            val codigo = resp.codigoError
                            if(codigo==1){
                                (cont as Activity).finish()
                                Toast.makeText(cont,"Contraseña cambiada correctamente",Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(cont,"Error al cambiar la contraseña",Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            )
        }

    }

    private fun comprobarContraOld (passOld:String,pass:String,idActual:Int) {
        val cont = this
        doAsync {
            val objetoDao = FactoriaUsuarios.getUsuarioDao()
            val llamada = objetoDao.getUsuarioPorId(idActual)
            llamada.enqueue(
                object: Callback<List<DataUsuarios>>{
                    override fun onFailure(call: Call<List<DataUsuarios>>, t: Throwable) {
                        Toast.makeText(cont,"Error de conexión", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<List<DataUsuarios>>,
                        response: Response<List<DataUsuarios>>
                    ) {
                        val respuesta  = response.body()
                        if(respuesta!= null){
                            val array = Usuarios.convertir(respuesta)
                            if(array.size ==1){
                                val contraBD = array[0].contrasena
                                val nombre = array[0].nombre
                                if(contraBD==passOld){
                                    updateUser(idActual,nombre,pass)
                                }else{
                                    Toast.makeText(cont,"Contraseña actual incorrecta", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }

                }
            )

        }

    }
}