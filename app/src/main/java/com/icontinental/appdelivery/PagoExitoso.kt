package com.icontinental.appdelivery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class PagoExitoso : AppCompatActivity() {

    lateinit var buttonRetornar: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago_exitoso)

        buttonRetornar = findViewById(R.id.buttonVolverHome)

        buttonRetornar.setOnClickListener {
            onBackPressed()
        }
    }
}