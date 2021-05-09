package com.example.appmaskcycle.api

import com.google.gson.annotations.SerializedName

data class DataTiposMasc (
    @SerializedName("id")
    val id:Int,
    @SerializedName("nombre_t")
    val nombre_t:String,
    @SerializedName("duracion")
    val duracion:Int,
    @SerializedName("info_extra")
    val info_extra:String
)