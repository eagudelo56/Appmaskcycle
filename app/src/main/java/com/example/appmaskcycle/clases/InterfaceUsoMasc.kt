package com.example.appmaskcycle.clases

import com.example.appmaskcycle.api.DataCodigoError
import com.example.appmaskcycle.api.DataDispMasc
import com.example.appmaskcycle.api.DataUsoMasc
import retrofit2.Call

interface InterfaceUsoMasc {
    fun getUsoMascByUsuario(usr:Int) : Call<List<DataUsoMasc>>
    fun insertarUsoMasc(id:Int,idPack:Int,inicio:String,activa:String,horasVida:String,
    final:String,lavados:Int): Call<DataCodigoError>

}