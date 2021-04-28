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
    val t_info:String,
    @SerializedName("inicio ")
    val inicio :String,
    @SerializedName("activa ")
    val activa :Boolean,
    @SerializedName("horas_vida  ")
    val horas_vida  :String,
    @SerializedName("final")
    val final:String,
    @SerializedName("lavados")
    val lavados:Int
)