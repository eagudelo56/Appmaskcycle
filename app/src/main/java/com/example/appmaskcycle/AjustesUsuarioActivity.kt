package com.example.appmaskcycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AjustesUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes_usuario)

        title = getString(R.string.nav_ajustes_usr)
    }
}