package com.example.appmaskcycle.clases

import com.example.appmaskcycle.api.DataCodigoError
import com.example.appmaskcycle.api.DataDispMasc
import retrofit2.Call

interface InterfaceDispMasc {

    fun getDispMascByUsuario(usr:Int) : Call<List<DataDispMasc>>
    /*
    fun getDispMascPorId (id:Int): Call<List<DataDispMasc>>*/
    fun insertarDispMasc(nombre:String,tipo:Int,lavados:Int,duracion:Int,stock:Int,comentario:String,idUsuarios:Int): Call<DataCodigoError>
    fun updateDispMasc(nombre:String,tipo:Int,lavados:Int,duracion:Int,stock:Int,comentario:String,idUsuarios:Int): Call<DataCodigoError>


}