package com.example.appmaskcycle.clases

import com.example.appmaskcycle.api.DataDispMasc
import retrofit2.Call

interface InterfaceDispMasc {

    fun getDispMascPorNombre (nombre:String) : Call<List<DataDispMasc>>
    fun getDispoMascPorId (id:Int): Call<List<DataDispMasc>>


}