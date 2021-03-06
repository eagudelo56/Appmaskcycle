package com.example.appmaskcycle.api

import com.google.gson.annotations.SerializedName

data class DataUsoMasc (
    @SerializedName("id")
    val id:Int,
    @SerializedName("nombre")
    val nombre:String,
    @SerializedName("tipo")
    val tipo:String,
    @SerializedName("t_info")
    val tInfo:String,
    @SerializedName("inicio")
    val inicio :String,
    @SerializedName("activa")
    val activa :String,
    @SerializedName("horas_vida")
    val horasVida:String,
    @SerializedName("final")
    val final:String,
    @SerializedName("lavados")
    val lavados:Int,
    @SerializedName("lavar")
    val lavar:String
)