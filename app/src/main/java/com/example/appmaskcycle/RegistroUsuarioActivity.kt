package com.example.appmaskcycle

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.appmaskcycle.api.DataCodigoError
import com.example.appmaskcycle.clases.FactoriaUsuarios
import kotlinx.android.synthetic.main.activity_registro_usuario.*
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)

        btnRegis.setOnClickListener{
            validarForm()
        }
    }

    private fun validarForm(){
        val usr = etUser.text.toString()
        val pas =  etContra.text.toString()
        val pasRepe =  etContraRepe.text.toString()
        if(usr.isNotEmpty() && pas.isNotEmpty() && pasRepe.isNotEmpty()){
            if(pas.length>=4){
                if(pas == pasRepe){
                    insertarUserDb(usr,pas)
                }else{
                    Toast.makeText(this,"Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this,"La contraseña debe tener 4 caracteres como minimo", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this,"Datos Incompletos", Toast.LENGTH_LONG).show()
        }
    }

    private fun insertarUserDb(nombre:String, contrasena:String){
        val cont = this
        doAsync {
            val objDao = FactoriaUsuarios.getUsuarioDao()
            val llamada = objDao.insertarUsuario(nombre,contrasena)
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
                                Toast.makeText(cont,"Cuenta creada",Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(cont,"El nombre de usuario ya existe",Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                }
            )
        }
    }
}