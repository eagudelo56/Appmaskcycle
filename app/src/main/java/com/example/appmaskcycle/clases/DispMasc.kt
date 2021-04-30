package com.example.appmaskcycle.clases


import com.example.appmaskcycle.api.Conexion
import com.example.appmaskcycle.api.DataDispMasc


class DispMasc(var id:Int, var nombre:String,var tipo:Int,
               var tInfo:String,var lavados:Int,
               var duracion:Int,var stock :Int,var comentario:String) : InterfaceDispMasc {

    private val c = Conexion()

    companion object{
        fun convertir (lista:List<DataDispMasc>): ArrayList<DispMasc>{
            val arrayL:ArrayList<DispMasc> = ArrayList()
            for( i:Int in lista.indices){
                val aux = DispMasc (lista[i].id, lista[i].nombre, lista[i].tipo,
                    lista[i].tInfo, lista[i].lavados, lista[i].duracion,
                    lista[i].stock, lista[i].comentario
                )
            }

            return arrayL
        }
    }

    /*override fun getDispMascPorNombre(nombre: String): Call<List<DataDispMasc>> {
        TODO("Not yet implemented")
    }

    override fun getDispoMascPorId (id:Int): Call<List<DataDispMasc>> {
        TODO("Not yet implemented")
    }*/
}
