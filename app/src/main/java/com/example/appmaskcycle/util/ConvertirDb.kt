package com.example.appmaskcycle.util

import java.text.SimpleDateFormat
import java.util.*

class ConvertirDb {

    companion object {
        private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

        fun getCalendarFromString(str:String) : Calendar {
            val cal = Calendar.getInstance()
            val aux = sdf.parse(str)
            if(aux!=null){
                cal.time = aux
            }
            return cal
        }

        fun getStringFromCalendar(cal:Calendar):String{
            val aux = cal.time
            return sdf.format(aux)
        }

        /*
         1 = true

         0 = false
        * */
        fun getBooleanFromString(str:String):Boolean{
            var bool = false
            if(str == "1"){
                bool = true
            }
            if(str == "0"){
                bool = false
            }
            return bool
        }

        fun getStringFromBoolean(bool:Boolean) : String{
            var str = "0" //* 1 es activa y 0 es en pausa o inactiva*//
            if(bool){
                str = "1"
            }
            if(!bool){
                str = "0"
            }
            return str
        }

    }


}