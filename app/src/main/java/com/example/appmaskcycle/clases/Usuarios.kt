package com.example.appmaskcycle.clases

import com.example.appmaskcycle.api.Conexion
import com.example.appmaskcycle.api.DataUsuarios
import retrofit2.Call

class Usuarios(val id:Int,val nombre:String,val contrasena:String) : InterfaceUsuarios {

    private val c = Conexion()

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

    override fun getUsuarioPorNombre(nombre:String): Call<List<DataUsuarios>> {
        return  c.executeQueryUsuario("select * from usuarios where NOMBRE='$nombre'")
    }

    override fun getUsuarioPorId(id:Int): Call<List<DataUsuarios>> {
        return  c.executeQueryUsuario("select * from usuarios where ID=$id")
    }





}