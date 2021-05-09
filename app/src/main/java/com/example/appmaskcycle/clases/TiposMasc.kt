package com.example.appmaskcycle.clases

import com.example.appmaskcycle.api.Conexion
import com.example.appmaskcycle.api.DataTiposMasc
import retrofit2.Call

class TiposMasc (
    var id:Int,
    var nombre_t:String,
    var duracion:Int,
    var info_extra:String
) : InterfaceTiposMasc {
    private val c = Conexion()

    companion object{
        fun convertir (lista:List<DataTiposMasc>):ArrayList<TiposMasc> {
            val arrayL:ArrayList<TiposMasc> = ArrayList()
            for (i in lista.indices){
                val aux = TiposMasc (lista[i].id, lista[i].nombre_t, lista[i].duracion,
                    lista[i].info_extra)
                arrayL.add(aux)
            }
            return arrayL
        }
    }

    override fun getAllTiposMasc(): Call<List<DataTiposMasc>> {
        val sql = "select * from tipos_masc"
        return  c.executeQueryTiposMasc(sql)
    }
}