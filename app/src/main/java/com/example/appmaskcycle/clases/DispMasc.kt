package com.example.appmaskcycle.clases


import com.example.appmaskcycle.api.Conexion
import com.example.appmaskcycle.api.DataCodigoError
import com.example.appmaskcycle.api.DataDispMasc
import retrofit2.Call


class DispMasc(var id:Int, var nombre:String,var tipo:String,
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
                arrayL.add(aux)
            }

            return arrayL
        }
    }

    override fun getDispMascByUsuario(usr:Int): Call<List<DataDispMasc>> {
        val sql = "select d.id,d.nombre,t.nombre_t 'tipo',t.info_extra 't_info'," +
        "d.lavados,d.duracion,d.stock,d.comentario " +
        "from disp_masc d " +
        "join (tipos_masc t) " +
        "on (d.tipo = t.id) " +
        "where d.id_usuario=$usr;"
        return  c.executeQueryDispMasc(sql)
    }

    override fun insertarDispMasc(
        nombre: String,
        tipo: Int,
        lavados: Int,
        duracion: Int,
        stock: Int,
        comentario: String,
        idUsuarios: Int
    ): Call<DataCodigoError> {

        val sql = "insert into disp_masc (nombre,tipo,lavados,duracion,stock,comentario, id_usuario) " +
                "values ('$nombre',$tipo,$lavados,$duracion,$stock,'$comentario',$idUsuarios)"
        return c.execute(sql)

    }

    override fun updateDispMasc(
        nombre: String,
        tipo: Int,
        lavados: Int,
        duracion: Int,
        stock: Int,
        comentario: String,
        idUsuarios: Int
    ): Call<DataCodigoError> {
        val sql  = " update disp_masc set nombre = '$nombre' " +
                "tipo =  $tipo , lavados = $lavados , duracion = $duracion , stock = $stock , "+
                " comentario = '$comentario', id_usuario = $idUsuarios ; "
        return  c.execute(sql)
    }

    /*
    override fun getDispoMascPorId (id:Int): Call<List<DataDispMasc>> {
        TODO("Not yet implemented")
    }*/




}