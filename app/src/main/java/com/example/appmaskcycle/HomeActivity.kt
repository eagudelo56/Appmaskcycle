package com.example.appmaskcycle

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmaskcycle.api.DataCodigoError
import com.example.appmaskcycle.api.DataDispMasc
import com.example.appmaskcycle.api.DataUsoMasc
import com.example.appmaskcycle.clases.*
import com.example.appmaskcycle.util.AdaptadorDisp
import com.example.appmaskcycle.util.AdaptadorUso
import com.example.appmaskcycle.util.ConvertirDb
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity() {

    //companion object var pantalla??????

    private var pantalla = 0
    //private var xInicio = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        /*
        *
        * title es un atributo de la actividad
        * con getString se obtienen los textos del archivo strings
        *
        * */
        title = getString(R.string.titulo_home)

        btnAddDisp.setOnClickListener{
            startActivity(Intent(this,AnadirDispActivity::class.java))
        }

        rvHome.layoutManager = LinearLayoutManager(this)

        tvUso.setOnClickListener{
            pantalla = 0
            cambiarPantalla()
        }

        tvDisp.setOnClickListener {
            pantalla = 1
            cambiarPantalla()
        }

        /*
        rvHome.setOnTouchListener(object : View.OnTouchListener {
            var continuar = false
            //continuar es para que cambiarPantalla() se
            //ejecute solo una vez cuando se desliza el dedo
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val eventX = event.x
                val logitudMovimineto = 300
                //eventX es la posicion de la pantalla que se pulsa
                //logitudMovimineto es la distancia que hay que recorrer
                // para que se llame a cambiarPantalla()
                when (event.action) {
                    //cuando se toca el rv por primera vez
                    MotionEvent.ACTION_DOWN -> {
                        xInicio = eventX
                        continuar = true
                    }
                    //cuando se mueve el dedo por la pantalla
                    MotionEvent.ACTION_MOVE -> {
                        //mueve el dedo a la derecha
                        //hace lo mismo que tvUso
                        if (eventX > (xInicio + logitudMovimineto)
                                && continuar) {
                            continuar = false
                            pantalla = 0
                            cambiarPantalla()
                        }
                        //mueve el dedo a la izquierda
                        //hace lo mismo que tvDisp
                        if (eventX < (xInicio - logitudMovimineto)
                                && continuar) {
                            continuar = false
                            pantalla = 1
                            cambiarPantalla()
                        }
                    }
                    //cuando se levanta el dedo
                    MotionEvent.ACTION_UP -> {
                        continuar = false
                    }
                }
                return true
            }
        })*/
    }


    /* 0 == USO Y 1 == DISPONIBLES*/
    private fun cambiarPantalla (){
        val usr = Usuarios.idActual
        if(usr!=null){ /* estamos en disponibles y cambiamos a la uso*/
            revisarUso(usr)
            comprobarEliminarDisp(usr)
            if(pantalla==0){
                tvUso.setTextColor(Color.GREEN)
                tvDisp.setTextColor(Color.GRAY)
                btnAddDisp.visibility = View.INVISIBLE
                recuperarEnUso(usr)
            }
            if(pantalla==1){ /*estamos en uso y cambiamos a en disponibles */
                tvUso.setTextColor(Color.GRAY)
                tvDisp.setTextColor(Color.GREEN)
                btnAddDisp.visibility = View.VISIBLE
                recuperarDisponibles(usr)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        cambiarPantalla()
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


    private fun revisarUso(usr:Int) {
        val cont = this
        doAsync {
            val objDAO = FactoriaUsoMasc.getUsoMascDao()
            val llamada = objDAO.getUsoMascByUsuario(usr)
            llamada.enqueue( /*con este meto EJECUTAMOS la llamada*/
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
                            for(i in array){
                                if(i.final.timeInMillis<Calendar.getInstance().timeInMillis
                                    && !i.activa){
                                    eliminarUso(i.id)
                                }else{

                                    val pausa = Calendar.getInstance().timeInMillis

                                    val final = i.final.timeInMillis
                                    val diferencia = final.minus(pausa)

                                    i.horasVida.timeInMillis = diferencia

                                    actualizarUso(
                                        i.id,
                                        ConvertirDb.getStringFromCalendar(i.inicio),
                                        ConvertirDb.getStringFromBoolean(i.activa),
                                        ConvertirDb.getStringFromCalendar(i.horasVida),
                                        ConvertirDb.getStringFromCalendar(i.final),
                                        i.lavados
                                    )
                                }
                            }
                        }
                    }
                })
        }
    }

    private fun actualizarUso (id:Int, inicio: String,
                               activa: String,
                               horasVida: String,
                               final: String,
                               lavados: Int) {
        val cont = this
        doAsync {
            val objDao = FactoriaUsoMasc.getUsoMascDao()
            val llamada = objDao.updateUsoMasc(id, inicio,
                activa, horasVida,
                final, lavados)
            llamada.enqueue(
                object : Callback<DataCodigoError> {
                    override fun onFailure(call: Call<DataCodigoError>, t: Throwable) {
                        Toast.makeText(cont,t.localizedMessage, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<DataCodigoError>,
                        response: Response<DataCodigoError>
                    ) {
                        val respuesta = response.body()
                        if(respuesta!=null){
                            val codigo = respuesta.codigoError
                            if(codigo == 1){
                                //Toast.makeText(cont,"bien", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(cont,"mal", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                }
            )
        }

    }


    private fun comprobarEliminarDisp(usr:Int) {
        /*
        * se llama en cambiarPantalla
        * se recuperan las disp y stock<=0 se comprueba
        * que no haya mask en uso de ese pack (comprobarPackEnUso)
        * si no hay ninguna en uso se elimina el pack
        * */
        val cont = this
        doAsync {
            val objDAO = FactoriaDispMasc.getDispMascDao()
            val llamada = objDAO.getDispMascByUsuario(usr)
            llamada.enqueue( /*con este meto EJECUTAMOS la llamada*/
                object : Callback<List<DataDispMasc>>{
                    override fun onFailure(call: Call<List<DataDispMasc>>, t: Throwable) {
                        Toast.makeText(cont,t.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(
                        call: Call<List<DataDispMasc>>,
                        response: Response<List<DataDispMasc>>
                    ) {
                        val respuesta = response.body()
                        if(respuesta!=null) {
                            val array = DispMasc.convertir(respuesta)
                            for(i in array){
                                if(i.stock<=0){
                                    comprobarPackEnUso(i.id)
                                }
                            }
                        }
                    }
                })
        }
    }


    private fun comprobarPackEnUso (id: Int) {
        val cont = this
        doAsync {
            val objDao = FactoriaUsoMasc.getUsoMascDao()
            val llamada = objDao.getUsoMascPorIdPack(id)
            llamada.enqueue(
                object : Callback<List<DataUsoMasc>>{
                    override fun onFailure(call: Call<List<DataUsoMasc>>, t: Throwable) {
                        Toast.makeText(cont,t.localizedMessage, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<List<DataUsoMasc>>,
                        response: Response<List<DataUsoMasc>>
                    ) {
                        val lista = response.body()
                        if(lista!=null){
                            if(lista.isEmpty()){
                                eliminarDisp(id)
                            }
                        }
                    }
                }
            )
        }
    }

    private fun eliminarDisp(id: Int) {
        val cont = this
        doAsync {
            val objDao = FactoriaDispMasc.getDispMascDao()
            val llamada = objDao.deleteDispMasc(id)
            llamada.enqueue(
                object : Callback<DataCodigoError>{
                    override fun onFailure(call: Call<DataCodigoError>, t: Throwable) {
                        Toast.makeText(cont,t.localizedMessage, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<DataCodigoError>,
                        response: Response<DataCodigoError>
                    ) {
                        val respuesta = response.body()
                        if(respuesta!=null){
                            val codigo = respuesta.codigoError
                            if(codigo == 1){
                                Toast.makeText(cont,"bien", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(cont,"mal", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                }
            )
        }
    }

    private fun eliminarUso(id: Int) {
        val cont = this
        doAsync {
            val objDao = FactoriaUsoMasc.getUsoMascDao()
            val llamada = objDao.deleteUsoMasc(id)
            llamada.enqueue(
                object : Callback<DataCodigoError>{
                    override fun onFailure(call: Call<DataCodigoError>, t: Throwable) {
                        Toast.makeText(cont,t.localizedMessage, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<DataCodigoError>,
                        response: Response<DataCodigoError>
                    ) {
                        val respuesta = response.body()
                        if(respuesta!=null){
                            val codigo = respuesta.codigoError
                            if(codigo == 1){
                                Toast.makeText(cont,"bien", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(cont,"mal", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                }
            )
        }
    }

    //se implementa el menu del documento xml a la actividad
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.layout_menu, menu)
        return true
    }


    //eventos del menu según la id del item pulsado
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //cuando se pulsa el item con id menu_itm_1
            R.id.menuAjusteUsuario -> {
                startActivity(Intent(this,AjustesUsuarioActivity::class.java))
                true
            }
            R.id.menuInfoMasc -> {
                startActivity(Intent(this,InfoMascActivity::class.java))
                true
            }
            R.id.menuManual -> {
                startActivity(Intent(this,ManualActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}