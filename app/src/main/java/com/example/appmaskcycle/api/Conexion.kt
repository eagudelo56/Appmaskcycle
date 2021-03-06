package com.example.appmaskcycle.api

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Conexion {

    private val ip:String = "http://redcapcrd.es/appmaskcycle/"
    private val apiKey:String = "appmaskcycleProyecto2021"

    private val clienteHttp = OkHttpClient().newBuilder()
         .connectTimeout(15, TimeUnit.SECONDS)
         .readTimeout(15, TimeUnit.SECONDS)
         .writeTimeout(15, TimeUnit.SECONDS)
         .build()

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ip)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())/*todas las librerias json para parsear el json*/
            .build()/*crear el objecto*/
    }

    fun executeQueryUsuario(sql:String) : Call<List<DataUsuarios>> {
        return getRetrofit().create(APIService::class.java).executeQueryApiUsuarios(sql,apiKey)
        /* llamar al metodo interface le pasamos el sql y api */
    }

    fun executeQueryDispMasc (sql: String): Call<List<DataDispMasc>>{
        return getRetrofit().create(APIService::class.java).executeQueryApiDispMasc(sql,apiKey)
        /* llamar al metodo interface le pasamos el sql y api */
    }

    fun executeQueryUsoMasc (sql: String): Call<List<DataUsoMasc>>{
        return getRetrofit().create(APIService::class.java).executeQueryApiUsoMasc(sql,apiKey)
        /* llamar al metodo interface le pasamos el sql y api */
    }

    fun executeQueryTiposMasc (sql: String): Call<List<DataTiposMasc>>{
        return getRetrofit().create(APIService::class.java).executeQueryApiTiposMasc(sql,apiKey)
        /* llamar al metodo interface le pasamos el sql y api */
    }

    fun execute(sql: String): Call<DataCodigoError>
    {
        return getRetrofit().create(APIService::class.java).executeApi(sql, apiKey)
        /* llamar al metodo interface le pasamos el sql y api */
    }
}