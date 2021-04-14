package com.example.appmaskcycle.api


import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIService {
    @POST("executeQuery.php")
    @FormUrlEncoded
    fun executeQueryApiUsuarios(
        @Field("sql") sql:String,
        @Field("apiKey") apiKey:String): Call<List<DataUsuarios>>


}

