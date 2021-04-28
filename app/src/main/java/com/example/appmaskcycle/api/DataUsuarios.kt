package com.example.appmaskcycle.api

import com.google.gson.annotations.SerializedName

data class DataUsuarios (

    @SerializedName("id")
    val id:Int,
    @SerializedName("nombre")
    val nombre:String,
    @SerializedName("contrasena")
    val contrasena:String


)