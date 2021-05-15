package com.example.appmaskcycle.clases

import com.example.appmaskcycle.api.Conexion
import com.example.appmaskcycle.api.DataCodigoError
import com.example.appmaskcycle.api.DataUsuarios
import retrofit2.Call

class Usuarios(var id:Int,var nombre:String,var contrasena:String) : InterfaceUsuarios {

    private val c = Conexion()

    companion object{
        var idActual : Int? = null

        fun convertir (lista:List<DataUsuarios>):ArrayList<Usuarios> {

            val arrayL:ArrayList<Usuarios> = ArrayList()
            for (i in lista.indices){
                val aux = Usuarios (lista[i].id, lista[i].nombre, lista[i].contrasena)
                arrayL.add(aux)
            }

            return arrayL
        }
    }

    override fun getUsuarioPorNombre(nombre:String): Call<List<DataUsuarios>> {
        return  c.executeQueryUsuario("select * from usuarios where nombre='$nombre'")
    }

    override fun getUsuarioPorId(id:Int): Call<List<DataUsuarios>> {
        return  c.executeQueryUsuario("select * from usuarios where id=$id")
    }

    override fun insertarUsuario(nombre: String, contrasena: String): Call<DataCodigoError> {
        val sql = "insert into usuarios " +
                "(nombre, contrasena) " +
                "VALUES ('$nombre', '$contrasena'); "
        return c.execute(sql)

    }

    override fun updateUsuario(id:Int, nombre: String, contrasena: String): Call<DataCodigoError> {
        val sql = "update usuarios set nombre  = '$nombre' ," +
                " contrasena = '$contrasena where id = $id;"
        return c.execute(sql)
    }

    override fun deleteUsuario(id: Int): Call<DataCodigoError> {
        val sql  = "delete from usuarios where id = $id;"
        return  c.execute(sql)
    }


}