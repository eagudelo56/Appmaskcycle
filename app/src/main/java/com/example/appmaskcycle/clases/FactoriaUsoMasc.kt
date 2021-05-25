package com.example.appmaskcycle.clases

import java.util.*

class FactoriaUsoMasc {
    companion object{
        fun getUsoMascDao() : UsoMasc{
            return UsoMasc (-1,"","","",
                Calendar.getInstance(),false,
                Calendar.getInstance(),Calendar.getInstance(),-1, false)
        }
    }
}