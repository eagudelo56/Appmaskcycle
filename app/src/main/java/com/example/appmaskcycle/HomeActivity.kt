package com.example.appmaskcycle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmaskcycle.api.DataDispMasc
import com.example.appmaskcycle.api.DataUsoMasc
import com.example.appmaskcycle.clases.*
import com.example.appmaskcycle.util.AdaptadorDisp
import com.example.appmaskcycle.util.AdaptadorUso
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private var pantalla = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        /*
        *
        * title es un atrivuto de la actividad
        * con getString se obtienen los textos del archivo strings
        *
        * */
        title = getString(R.string.titulo_home)

        btnAddDisp.setOnClickListener{
            startActivity(Intent(this,AnadirDispActivity::class.java))
        }

        rvHome.layoutManager = LinearLayoutManager(this)

        tvUso.setOnClickListener{
            cambiarPantalla()
        }

        tvDisp.setOnClickListener {
            cambiarPantalla()
        }

    }
    /* 0 == USO Y 1 == DISPONIBLES*/
    private fun cambiarPantalla (){
        val usr = Usuarios.idActual
        if(usr!=null){ /* estamos en uso y cambiamos a la disponibles*/
            if(pantalla==0){
                btnAddDisp.visibility = View.VISIBLE
                recuperarDisponibles(usr)
                pantalla=1
            }
            if(pantalla==1){ /*estamos en disponibles y cambiamos a en uso */
                btnAddDisp.visibility = View.INVISIBLE
                recuperarEnUso(usr)
                pantalla=0
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val usr = Usuarios.idActual
        if(usr!=null){
            recuperarDisponibles(usr)
        }

    }

    private fun actualizarRVDisp(array:ArrayList<DispMasc>){
        rvHome.adapter = AdaptadorDisp(this, array)
    }

    private fun actualizarRVUso(array:ArrayList<UsoMasc>){
        rvHome.adapter = AdaptadorUso(this, array)
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
                            actualizarRVDisp(array)
                        }
                    }
                })
        }
    }

    private fun recuperarEnUso (usr: Int) {
        val cont =this
        doAsync {
            val objDAO = FactoriaUsoMasc.getUsoMascDao()
            val llamada = objDAO.getUsoMascByUsuario(usr)
            llamada.enqueue(
                object : Callback<List<DataUsoMasc>>{
                    override fun onFailure(call: Call<List<DataUsoMasc>>, t: Throwable) {
                        Toast.makeText(cont,t.localizedMessage, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<List<DataUsoMasc>>,
                        response: Response<List<DataUsoMasc>>
                    ) {
                        val respuesta = response.body()
                        if(respuesta!=null) {
                            val array = UsoMasc.convertir(respuesta)
                            actualizarRVUso(array)
                        }
                    }

                }
            )


        }
    }



}