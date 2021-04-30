package com.example.appmaskcycle.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @Headers("Accept: text/html")
    @POST("executeQuery.php")
    @FormUrlEncoded
    fun executeQueryApiUsuarios(
        @Field("sql") sql:String,
        @Field("key") apiKey:String): Call<List<DataUsuarios>>

    @Headers("Accept: text/html")
    @POST("executeQuery.php")
    @FormUrlEncoded
    fun executeQueryApiDispMasc (
        @Field("sql") sql:String,
        @Field("key") apiKey:String): Call<List<DataDispMasc>>

    @Headers("Accept: text/html")
    @POST("executeQuery.php")
    @FormUrlEncoded
    fun executeQueryApiUsoMasc (
        @Field("sql") sql:String,
        @Field("key") apiKey:String): Call<List<DataUsoMasc>>

    @Headers("Accept: text/html")
    @POST("executeQuery.php")
    @FormUrlEncoded
    fun executeQueryApiTiposMasc (
        @Field("sql") sql:String,
        @Field("key") apiKey:String): Call<List<DataTiposMasc>>

}



