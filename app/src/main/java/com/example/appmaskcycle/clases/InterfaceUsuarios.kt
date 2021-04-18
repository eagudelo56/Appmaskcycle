package com.example.appmaskcycle.clases

import com.example.appmaskcycle.api.DataUsuarios
import retrofit2.Call

interface InterfaceUsuarios {

    fun getUsuarioPorNombre(nombre:String): Call<List<DataUsuarios>>
    fun getUsuarioPorId(id:Int): Call<List<DataUsuarios>>
}