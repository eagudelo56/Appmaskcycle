package com.example.appmaskcycle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appmaskcycle.api.DataCodigoError
import com.example.appmaskcycle.api.DataTiposMasc
import com.example.appmaskcycle.clases.FactoriaDispMasc
import com.example.appmaskcycle.clases.FactoriaTiposMasc
import com.example.appmaskcycle.clases.TiposMasc
import com.example.appmaskcycle.clases.Usuarios
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_anadir_disp.*
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AnadirDispActivity : AppCompatActivity() {

    private var arrTiposGloval: ArrayList<TiposMasc>? = null
    private var arrayOcultar = arrayOf(4,5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_disp)


        title = getString(R.string.titulo_anadir_disp)

        recuperarTiposDb()

        btnAnadir.setOnClickListener{
            validarFormulario()
        }

        btnQR.setOnClickListener{
            initScan()
        }

    }

    private fun initScan() {
        val integrator = IntentIntegrator(this)

        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escanea el codigo qr del paquete de las mascarillas")
        integrator.setBeepEnabled(true)

        integrator.initiateScan()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if(result!=null){
            if(result.contents==null){
                Toast.makeText(this, "CANCELADO", Toast.LENGTH_SHORT).show()
            }
            else{
                //pos 0 = clave, pos 1 = id tipo, pos 2 = stock
                val arrQR = result.contents.split("-")
                val claveQR = "AppMaskCycle"
                val stockQR = arrQR[2]
                if(arrQR[0] == claveQR){
                    val idTipo = arrQR[1].toInt()
                    val arr = arrTiposGloval
                    if(arr!=null){
                        for(i in arr){
                            if(i.id == idTipo){
                                spTipos.setSelection(i.id-1)
                                etNombre.setText(i.nombre_t)
                                etStock.setText(stockQR)
                                etLavados.setText(0.toString())
                                etDuracion.setText(i.duracion.toString())
                                Toast.makeText(this, "ESCANEADO CON EXITO", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }else{
                    Toast.makeText(this, "CODIGO QR NO VALIDO", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    private fun validarFormulario () {
        var relleno = true
        val usuario = Usuarios.idActual
        val arrayEt = arrayOf(etNombre,etStock)
        val arrTip = arrTiposGloval
        val tipoMarcado = spTipos.selectedItemPosition

        for ( i in arrayEt) {
            if(i.text.toString().isEmpty()){
                relleno=false
            }
        }

        if(relleno && usuario!=null && arrTip!=null){
            var durac = 0
            var lavados = 0
            if(comprobarMostrar(tipoMarcado)){
                durac = etDuracion.text.toString().toInt()
                if(durac>23){
                    durac = 23
                }
                lavados = etLavados.text.toString().toInt()
            }else{
                durac = arrTip[tipoMarcado].duracion
            }
            val idTipo = arrTip[tipoMarcado].id
            insertarBD(etNombre.text.toString(),
                    idTipo,lavados,durac,
                    etStock.text.toString().toInt(),
                    etComent.text.toString(),usuario)
        }
    }



    private fun insertarBD (nombre:String, tipo:Int, lavados:Int, duracion:Int, stock:Int,comentario:String, usuario:Int) {
        val cont=this
            doAsync {
                val objetoDao = FactoriaDispMasc.getDispMascDao()
                val llamada =
                    objetoDao.insertarDispMasc(nombre,tipo,lavados,duracion
                        ,stock, comentario, usuario)
                llamada.enqueue(
                    object : Callback<DataCodigoError> {
                        override fun onFailure(call: Call<DataCodigoError>, t: Throwable) {
                            Toast.makeText(cont,"no conexion",Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(
                            call: Call<DataCodigoError>,
                            response: Response<DataCodigoError>
                        ) {
                            val respuesta = response.body()
                            if(respuesta!=null){
                                if(respuesta.codigoError==1){
                                    Toast.makeText(cont,"Mascarilla insertada",Toast.LENGTH_LONG).show()
                                    (cont as Activity).finish()/*acaba la activitdad del fomulario*/

                                }
                                else if(respuesta.codigoError==0){
                                    Toast.makeText(cont,"Mal "+respuesta.codigoError,Toast.LENGTH_LONG).show()
                                }
                                else{
                                    Toast.makeText(cont,"ni 0 ni 1",Toast.LENGTH_LONG).show()
                                }
                            }
                            else{
                                Toast.makeText(cont,"Servidor ha fallado",Toast.LENGTH_LONG).show()
                            }
                        }

                    }
                )
            }
    }

    private fun recuperarTiposDb(){
        val cont = this
        val objetoDao = FactoriaTiposMasc.getTiposMascDao()
        val llamada = objetoDao.getAllTiposMasc()
        llamada.enqueue(
            object : Callback<List<DataTiposMasc>> {
                override fun onFailure(call: Call<List<DataTiposMasc>>, t: Throwable) {
                    Toast.makeText(cont,"no conexion",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<List<DataTiposMasc>>,
                    response: Response<List<DataTiposMasc>>
                ) {
                    val respuesta = response.body()
                    if(respuesta!=null){
                        val arrTip = TiposMasc.convertir(respuesta)
                        /*se cambia el valor del atributo arrTipos
                        * por el array obtenido de la base de datos*/
                        arrTiposGloval = arrTip
                        inicializarSp(arrTip)
                    }
                }
            }
        )
    }

    private fun getStringSp(arrTipos: ArrayList<TiposMasc>):Array<String>{
        /* {""} es para inicializar todas las posiciones del array en cadena vacia*/
        val arrString = Array(arrTipos.size) {""}
        for(i in arrString.indices){
            arrString[i] = arrTipos[i].nombre_t
        }
        return arrString
    }

    private fun inicializarSp(arrTip: ArrayList<TiposMasc>){
        val listaString = getStringSp(arrTip)
        val adaptadorSpin = ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item, listaString)
        spTipos.adapter = adaptadorSpin
        anadirListenerSp()
    }

    private fun comprobarMostrar(posicion:Int):Boolean{
        var mostrar = false
        val arrTip = arrTiposGloval
        if(arrTip!=null){
            for(i in arrayOcultar.indices){
                if(arrTip[posicion].id==arrayOcultar[i]){
                    mostrar = true
                }
            }
        }
        return mostrar
    }

    private fun cambiarVisibilidad(){
        val tipoMarcado = spTipos.selectedItemPosition
        val mostrar = comprobarMostrar(tipoMarcado)
        if(mostrar){
            contenedor.visibility = View.VISIBLE
        }else{
            contenedor.visibility = View.INVISIBLE
        }
    }

    private fun anadirListenerSp(){
        spTipos.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                cambiarVisibilidad()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                contenedor.visibility = View.INVISIBLE
            }
        }
    }

}