package com.example.appmaskcycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class InfoMascActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_masc)

        title = getString(R.string.nav_info_masc)

    }
}