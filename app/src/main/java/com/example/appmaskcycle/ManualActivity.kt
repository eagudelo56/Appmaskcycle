package com.example.appmaskcycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ManualActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual)

        title = getString(R.string.nav_manual)

    }
}