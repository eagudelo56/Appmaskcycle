package com.example.appmaskcycle.api

import com.google.gson.annotations.SerializedName

data class DataUsuarios (

    @SerializedName("ID")
    val id:Int,
    @SerializedName("NOMBRE")
    val nombre:String,
    @SerializedName("CONTRASENA")
    val contrasena:String


)