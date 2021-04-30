package com.example.appmaskcycle.clases

import com.example.appmaskcycle.api.Conexion
import com.example.appmaskcycle.api.DataTiposMasc
import com.example.appmaskcycle.api.DataUsuarios
import retrofit2.Call

class TiposMasc (
    var id:Int,
    var nombre:String,
    var tipo:Int,
    var lavados:Int,
    var duracion:Int,
    var stock:Int,
    var comentario:String
) : InterfaceTiposMasc {
    private val c = Conexion()

    companion object{
        fun convertir (lista:List<DataTiposMasc>):ArrayList<TiposMasc> {
            val arrayL:ArrayList<TiposMasc> = ArrayList<TiposMasc>()
            for (i in lista.indices){
                val aux = TiposMasc (lista[i].id, lista[i].nombre, lista[i].tipo,
                    lista[i].lavados,lista[i].duracion,lista[i].stock,
                    lista[i].comentario)
                arrayL.add(aux)
            }
            return arrayL
        }
    }

    override fun getAllTiposMasc(): Call<List<DataTiposMasc>> {
        /*return  c.executeQueryUsuario("select * from tipos_masc")*/
        TODO("Not yet implemented")
    }
}