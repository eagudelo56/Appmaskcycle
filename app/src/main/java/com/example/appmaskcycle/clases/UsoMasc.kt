package com.example.appmaskcycle.clases

import com.example.appmaskcycle.api.Conexion
import com.example.appmaskcycle.api.DataCodigoError
import com.example.appmaskcycle.api.DataUsoMasc
import retrofit2.Call

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

    override fun getUsoMascByUsuario(usr: Int): Call<List<DataUsoMasc>> {
        val sql = "select u.id, d.nombre,t.nombre_t 'tipo', t.info_extra 't_info', " +
                "u.inicio,u.activa,u.horas_vida,u.final,u.lavados " +
                "from uso_masc u " +
                "join (disp_masc d, tipos_masc t) " +
                "on (u.id_pack = d.id and d.id = t.id ) " +
                "where d.id_usuario=$usr;"

         return c.executeQueryUsoMasc(sql)
    }

    override fun insertarUsoMasc(
        id: Int,
        idPack: Int,
        inicio: String,
        activa: String,
        horasVida: String,
        final: String,
        lavados: Int
    ): Call<DataCodigoError> {
        TODO("Not yet implemented")
    }
}