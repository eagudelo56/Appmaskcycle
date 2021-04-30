package com.example.appmaskcycle.api

import com.google.gson.annotations.SerializedName


data class DataDispMasc (
    @SerializedName("id")
    val id:Int,
    @SerializedName("nombre")
    val nombre:String,
    @SerializedName("tipo")
    val tipo:String,
    @SerializedName("t_info")
    val tInfo:String,
    @SerializedName("lavados")
    val lavados:Int,
    @SerializedName("duracion")
    val duracion:Int,
    @SerializedName("stock")
    val stock :Int,
    @SerializedName("comentario")
    val comentario:String
)

