package com.example.appmaskcycle.clases

import com.example.appmaskcycle.api.DataCodigoError
import com.example.appmaskcycle.api.DataUsuarios
import retrofit2.Call

interface InterfaceUsuarios {

    fun getUsuarioPorNombre(nombre:String): Call<List<DataUsuarios>>
    fun getUsuarioPorId(id:Int): Call<List<DataUsuarios>>
    fun insertarUsuario(nombre:String,contrasena : String):Call<DataCodigoError>
    fun updateUsuario (nombre:String,contrasena : String):Call<DataCodigoError>
    fun deleteUsuario (id:Int):Call<DataCodigoError>
}