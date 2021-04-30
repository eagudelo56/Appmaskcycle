package com.example.appmaskcycle.clases

import com.example.appmaskcycle.api.DataTiposMasc
import retrofit2.Call

interface InterfaceTiposMasc {
    fun getAllTiposMasc(): Call<List<DataTiposMasc>>
}