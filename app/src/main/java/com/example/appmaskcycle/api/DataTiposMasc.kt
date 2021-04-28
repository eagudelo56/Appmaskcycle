package com.example.appmaskcycle.api

import com.google.gson.annotations.SerializedName

data class DataTiposMasc (
    @SerializedName("id")
    val id:Int,
    @SerializedName("nombre")
    val nombre:String,
    @SerializedName("tipo")
    val tipo:Int,
    @SerializedName("lavados")
    val lavados:Int,
    @SerializedName("duracion")
    val duracion:Int,
    @SerializedName("stock")
    val stock:Int,
    @SerializedName("comentario")
    val comentario:String
)