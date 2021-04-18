package com.example.appmaskcycle.clases

import com.example.appmaskcycle.api.DataUsuarios

class Usuarios(val id:Int,val nombre:String,val contrasena:String) {


    companion object{
        fun convertir (lista:List<DataUsuarios>):ArrayList<Usuarios> {

            val arrayL:ArrayList<Usuarios> = ArrayList<Usuarios>()
            for (i in lista.indices){
                val aux = Usuarios (lista[i].id, lista[i].nombre, lista[i].contrasena)
                arrayL.add(aux)
            }

            return arrayL
        }
    }




    fun hello ():String{
        return nombre
    }



}