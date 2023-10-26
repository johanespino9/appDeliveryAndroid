package com.icontinental.appdelivery

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PedidosActivity : AppCompatActivity() {

    lateinit var textViewNameCustomer: TextView
    lateinit var textViewAddressCustomer: TextView
    lateinit var textViewEstadoPedido: TextView
    lateinit var textViewMontoTotal: TextView
    lateinit var textViewId: TextView

    lateinit var db: FirebaseFirestore

    private lateinit var auth: FirebaseAuth

    lateinit var sharedPreferencePedido: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedidos)

        textViewNameCustomer = findViewById(R.id.nameCustomer)
        textViewAddressCustomer = findViewById(R.id.addressCustomer)
        textViewEstadoPedido = findViewById(R.id.estadoPedido)
        textViewMontoTotal = findViewById(R.id.montoTotal)
        textViewId = findViewById(R.id.numberPedido)

        sharedPreferencePedido = getSharedPreferences("PEDIDO_ACTUAL", Context.MODE_PRIVATE)

        db = Firebase.firestore

        auth = Firebase.auth

        obtenerDatosPedido()
    }

    fun obtenerDatosPedido() {
        val idPedido = sharedPreferencePedido.getString("pedido", "")
        if (idPedido == "") {
            return
        }

        val docRef = db.collection("pedidos").document(idPedido!!)
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d("DOCUMENTO", "DocumentSnapshot data: ${document.data}")
                val nombre = document.getString("nombres") ?: "Nombre no disponible"
                val direccion = document.getString("direccion") ?: "Dirección no disponible"
                val apellido = document.getString("apellido") ?: "Apellido no disponible"
                val estado = document.getString("estado") ?: "Estado no disponible"
                val montoTotal = document.getDouble("precio") ?: "0"
                val id = document.getLong("id") ?: 1

                val nombreCompleto = nombre + " " + apellido
                val monto = "S/ "+montoTotal.toString()
                textViewNameCustomer.text = nombreCompleto.toString()
                textViewAddressCustomer.text =  direccion.toString()
                textViewEstadoPedido.text =  estado.toString()
                textViewMontoTotal.text = monto.toString()

                textViewId.text = "Pedido #"+id.toString()


            } else {
                Log.d("NO PEDIDO", "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d("ERROR", "get failed with ", exception)
        }

    }
}