package com.example.appmaskcycle.clases

import com.example.appmaskcycle.api.DataDispMasc
import retrofit2.Call

interface InterfaceDispMasc {

    fun getAllDispMasc () : Call<List<DataDispMasc>>
    /*
    fun getDispMascPorId (id:Int): Call<List<DataDispMasc>>*/


}