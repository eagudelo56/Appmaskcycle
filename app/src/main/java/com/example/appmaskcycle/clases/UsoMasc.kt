package com.example.appmaskcycle.clases

import com.example.appmaskcycle.api.Conexion
import com.example.appmaskcycle.api.DataUsoMasc

class UsoMasc(var id:Int, var nombre:String, var tipo:String, var tInfo:String, var inicio :String,var activa :String,var horasVida:String, var final:String,var lavados:Int) : InterfaceUsoMasc {

    private val c = Conexion()

    companion object{
        fun convertir (lista:List<DataUsoMasc>):ArrayList<UsoMasc> {

            val arrayL:ArrayList<UsoMasc> = ArrayList()
            for (i in lista.indices){
                val aux = UsoMasc (lista[i].id, lista[i].nombre, lista[i].tipo, lista[i].tInfo, lista[i].inicio, lista[i].activa, lista[i].horasVida, lista[i].final, lista[i].lavados)
                arrayL.add(aux)
            }

            return arrayL

            //prueba1
        }
    }
}