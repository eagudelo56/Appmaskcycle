package com.example.appmaskcycle.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appmaskcycle.R
import com.example.appmaskcycle.clases.DispMasc
import kotlinx.android.synthetic.main.disp_masc.view.*

class AdaptadorDisp(var content:Context, var array:ArrayList<DispMasc>): RecyclerView.Adapter<AdaptadorDisp.ViewHolder>()
{
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(mascarilla: DispMasc) {
            //guarda el objeto de la poscion actual
            itemView.tvDispNombre.text = mascarilla.nombre
            itemView.tvDispStock.text = mascarilla.stock.toString()
        }
    }

    override fun getItemCount(): Int {
        return array.size
    }

    //por cada fila encontrada en la bd, asigna el layout de disp_masc
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.disp_masc, parent, false))
    }

    //por cada fila encontrada en la bd, rellena la informacion del arrayList
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = array[position]
        holder.bind(item)
    }
}