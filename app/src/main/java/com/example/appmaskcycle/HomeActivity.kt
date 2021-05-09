package com.example.appmaskcycle

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmaskcycle.api.DataDispMasc
import com.example.appmaskcycle.clases.DispMasc
import com.example.appmaskcycle.clases.FactoriaDispMasc
import com.example.appmaskcycle.clases.Usuarios
import com.example.appmaskcycle.util.AdaptadorDisp
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        btnAddDisp.setOnClickListener{
            startActivity(Intent(this,AnadirDispActivity::class.java))
        }

        rvHome.layoutManager = LinearLayoutManager(this)

    }

    override fun onResume() {
        super.onResume()

        val usr = Usuarios.idActual
        if(usr!=null){
            recuperarDisponibles(usr)
        }

    }

    private fun actualizarRV(array:ArrayList<DispMasc>){
        rvHome.adapter = AdaptadorDisp(this, array)
    }

    private  fun recuperarDisponibles(usr:Int) {
        //val cont =this
        doAsync {
            val objDAO = FactoriaDispMasc.getDispMascDao()
            val llamada = objDAO.getDispMascByUsuario(usr)
            llamada.enqueue( /*con este meto EJECUTAMOS la llamada*/
                object : Callback<List<DataDispMasc>>{
                    override fun onFailure(call: Call<List<DataDispMasc>>, t: Throwable) {
                        //(cont as Activity).tvPrueba.text = t.localizedMessage
                    }
                    override fun onResponse(
                        call: Call<List<DataDispMasc>>,
                        response: Response<List<DataDispMasc>>
                    ) {
                        val respuesta = response.body()
                        if(respuesta!=null) {
                            val array = DispMasc.convertir(respuesta)
                                actualizarRV(array)
                        }
                    }
                })
        }
    }
}