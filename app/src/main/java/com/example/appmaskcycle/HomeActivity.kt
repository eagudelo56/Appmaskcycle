package com.example.appmaskcycle

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appmaskcycle.api.DataDispMasc
import com.example.appmaskcycle.clases.DispMasc
import com.example.appmaskcycle.clases.FactoriaDispMasc
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)




        recuperarDisponibles()
    }

    private  fun recuperarDisponibles() {
        val cont =this

        doAsync {
            val objDAO = FactoriaDispMasc.getDispMascDao()
            val llamada = objDAO.getAllDispMasc()
            llamada.enqueue( /*con este meto EJECUTAMOS la llamada*/
                object : Callback<List<DataDispMasc>>{
                    override fun onFailure(call: Call<List<DataDispMasc>>, t: Throwable) {
                        (cont as Activity).tvPrueba.text = t.localizedMessage
                    }

                    override fun onResponse(
                        call: Call<List<DataDispMasc>>,
                        response: Response<List<DataDispMasc>>
                    ) {
                        val respuesta = response.body()
                        if(respuesta!=null) {
                            val array = DispMasc.convertir(respuesta)

                            val aux = array[0].id.toString() + "\n" +
                                    array[0].nombre + "\n"+
                                    array[0].tipo + "\n"+
                                    array[0].tInfo + "\n"+
                                    array[0].lavados + "\n"+
                                    array[0].duracion + "\n"+
                                    array[0].stock + "\n"+
                                    array[0].comentario

                            (cont as Activity).tvPrueba.text = aux
                        }
                    }

                })
        }

    }
}