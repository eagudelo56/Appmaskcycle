package com.example.appmaskcycle.clases

import com.example.appmaskcycle.api.Conexion
import com.example.appmaskcycle.api.DataCodigoError
import com.example.appmaskcycle.api.DataUsoMasc
import com.example.appmaskcycle.util.ConvertirDb
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList

class UsoMasc(var id:Int, var nombre:String, var tipo:String,
              var tInfo:String, var inicio :Calendar,
              var activa : Boolean ,var horasVida:Calendar,
              var final:Calendar, var lavados:Int) : InterfaceUsoMasc {

    private val c = Conexion()

    private fun horaFormato(cal:Calendar):String{
        var horaAux = ""
        var minAux = ""
        val hora = cal.get(Calendar.HOUR_OF_DAY)
        val min = cal.get(Calendar.MINUTE)
        if(hora<10){
            horaAux = "0"
        }
        if(min<10){
            minAux = "0"
        }
        return "$horaAux$hora:$minAux$min"
    }

    fun getHoraVformato() : String {
        return horaFormato(horasVida)
    }

    fun getInicioformato() : String {
        return horaFormato(inicio)
    }

    fun getFinFormato() : String {
        return horaFormato(final)
    }


    companion object{
        fun convertir (lista:List<DataUsoMasc>):ArrayList<UsoMasc> {

            val arrayL:ArrayList<UsoMasc> = ArrayList()
            for (i in lista.indices){
                val actv = ConvertirDb.getBooleanFromString(lista[i].activa)
                val inic = ConvertirDb.getCalendarFromString(lista[i].inicio)
                val horasV = ConvertirDb.getCalendarFromString(lista[i].horasVida)
                val fin = ConvertirDb.getCalendarFromString(lista[i].final)

                val aux = UsoMasc (lista[i].id, lista[i].nombre, lista[i].tipo,
                    lista[i].tInfo, inic, actv, horasV,
                    fin, lista[i].lavados)
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
                "on (u.id_pack = d.id and d.tipo = t.id ) " +
                "where d.id_usuario=$usr;"

         return c.executeQueryUsoMasc(sql)
    }

    override fun getUsoMascPorId(id: Int): Call<List<DataUsoMasc>> {
        val sql = "select u.id, d.nombre,t.nombre_t 'tipo', t.info_extra 't_info', " +
                "u.inicio,u.activa,u.horas_vida,u.final,u.lavados " +
                "from uso_masc u " +
                "join (disp_masc d, tipos_masc t) " +
                "on (u.id_pack = d.id and d.tipo = t.id ) " +
                "where u.id=$id;"
        return c.executeQueryUsoMasc(sql)
    }

    override fun getUsoMascPorIdPack(idPack: Int): Call<List<DataUsoMasc>> {
        val sql = "select u.id, d.nombre,t.nombre_t 'tipo', t.info_extra 't_info', " +
                "u.inicio,u.activa,u.horas_vida,u.final,u.lavados " +
                "from uso_masc u " +
                "join (disp_masc d, tipos_masc t) " +
                "on (u.id_pack = d.id and d.tipo = t.id ) " +
                "where u.id_pack=$idPack;"
        return c.executeQueryUsoMasc(sql)
    }

    override fun insertarUsoMasc(
        idPack: Int,
        inicio: String,
        activa: String,
        horasVida: String,
        final: String,
        lavados: Int
    ): Call<DataCodigoError> {
        val sql = "insert into uso_masc " +
                "(id_pack,inicio,activa,horas_vida,final,lavados) " +
                "VALUES ($idPack, '$inicio', '$activa', '$horasVida', " +
                "'$final', $lavados); "
        return c.execute(sql)
    }

    override fun updateUsoMasc(
        id:Int,
        inicio: String,
        activa: String,
        horasVida: String,
        final: String,
        lavados: Int
    ): Call<DataCodigoError> {
        val sql = "update uso_masc set" +
                " inicio = '$inicio', activa = '$activa', horas_vida = '$horasVida', " +
                " final = '$final' , lavados = $lavados where id = $id;"
        return c.execute(sql)
    }

    override fun deleteUsoMasc(id: Int): Call<DataCodigoError> {
        val sql  = "delete from uso_masc where id = $id;"
        return  c.execute(sql)
    }


}