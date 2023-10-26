package com.icontinental.appdelivery

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.icontinental.appdelivery.model.CarritoProducto
import com.icontinental.appdelivery.model.PedidoProductos
import com.icontinental.fooddeliveryapp.componentes.productos.AdapterCarritoProductos
import com.icontinental.fooddeliveryapp.componentes.productos.AdapterProductos
import java.text.SimpleDateFormat
import java.util.Calendar

class CarritoActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var productos: ArrayList<CarritoProducto>
    lateinit var pedidoProductos: ArrayList<PedidoProductos>
    lateinit var gson: Gson

    lateinit var recyclerViewProductos: RecyclerView
    lateinit var textViewTotal: TextView

    lateinit var buttonConfirmarPedido: AppCompatButton

    lateinit var db: FirebaseFirestore

    lateinit var sharedPreferenceUser: SharedPreferences

    lateinit var sharedPreferencePedido: SharedPreferences

    lateinit var sharedPreferenceIdPedido: SharedPreferences

    var sumaTotal: Double = 0.0

    var uid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)
        val user = FirebaseAuth.getInstance().currentUser
        uid = user!!.uid
        recyclerViewProductos = findViewById(R.id.recyclerviewProductosCarrito)
        gson = Gson()
        db = Firebase.firestore

        sharedPreferences = getSharedPreferences("USER_PREFERENCES", MODE_PRIVATE)
        sharedPreferenceUser = getSharedPreferences("USER_DATA_PREFERENCES", Context.MODE_PRIVATE)
        sharedPreferencePedido = getSharedPreferences("PEDIDO_ACTUAL", MODE_PRIVATE)
        sharedPreferenceIdPedido = getSharedPreferences(uid, MODE_PRIVATE)
        textViewTotal = findViewById(R.id.textViewTotalProductos)
        buttonConfirmarPedido = findViewById(R.id.buttonConfirmarPedido)
        buttonConfirmarPedido.isEnabled = false
        obtenerProductosMemoria()
//

//        val adapterProductos = AdapterProductos(productos, this, object : OnItemClickListener {
//            override fun onItemClick(position: Int) {
//                val producto = productos[position]
//                Log.d("PRODUCTO SELECCIONADO", "producto nombre${productos[position].nombre}")
//
////                val intent = Intent(contextPrueba, DetalleProducto::class.java)
////
////                intent.putExtra("nombre", producto.nombre ?: "")
////                intent.putExtra("categoria", producto.categoria ?: "")
////                intent.putExtra("imagen", producto.imagen ?: "")
////                intent.putExtra("precioDescuento", producto.precioDescuento ?: 0.0)
////                intent.putExtra("precioRegular", producto.precioRegular ?: 0.0)
////
////                startActivity(intent)
//            }
//        })
        val adapterProductos = AdapterCarritoProductos(productos,this)

//        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManagerProductos = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        recyclerViewProductos.layoutManager = linearLayoutManagerProductos
        recyclerViewProductos.adapter = adapterProductos


        buttonConfirmarPedido.setOnClickListener {
            guardarPedido()
        }
    }

    fun guardarPedido() {
        val guardarId = sharedPreferenceIdPedido.edit()
        var id = 0
        val nameUser = sharedPreferenceUser.getString("nombre", null)
        val lastNameUser = sharedPreferenceUser.getString("apellido", null)
        val addressUser = sharedPreferenceUser.getString("direccion", null)


        //val dataLocal = sharedPreferenceIdPedido.edit()
        if (sharedPreferenceIdPedido.getInt("id", 0) == 0) {
            id = 1
        } else {
            val idSave = sharedPreferenceIdPedido.getInt("id", 0)
            id =  idSave + 1
        }
        guardarId.putInt("id", id)
        guardarId.apply()

        pedidoProductos = CarritoToPedidoMapper.map(productos)

        val pedido = hashMapOf(
            "apellido" to lastNameUser,
            "direccion" to addressUser,
            "estado" to "Pendiente",
            "fecha" to getCurrentDateAsStringLegacy(),
            "id" to id,
            "nombres" to nameUser,
            "precio" to sumaTotal,
            "productos" to pedidoProductos
        )

            db.collection("pedidos")
                .add(pedido)
                .addOnSuccessListener { documentReference ->
                    val pedido = sharedPreferencePedido.edit()
                    pedido.putString("pedido", documentReference.id)
                    pedido.apply()

                    Log.d("REGISTRO PEDIDO", "DocumentSnapshot added with ID: ${documentReference.id}")
                    Toast.makeText(this, "Se registro nuevo pedido", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "DocumentSnapshot added with ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()

                    val productosJson = sharedPreferences.getString("carrito", null)
                    val editor = sharedPreferences.edit()
                    editor.clear()
                    editor.apply()

                    val intent = Intent(this, PagoExitoso::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.w("ERROR EN REGISTRO DE PEDIDO", "Error adding document", e)
                    Toast.makeText(this, "Hubo un error al registrar pedido", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "Error adding document: ${e}", Toast.LENGTH_SHORT).show()
                }


    }

    fun obtenerProductosMemoria(){
        val productosJson = sharedPreferences.getString("carrito", null)

        if (productosJson != null) {
            val type = object : TypeToken<ArrayList<CarritoProducto>>() {}.type
            productos = gson.fromJson(productosJson, type)
            sumaTotal =  calcularTotal(productos)
            textViewTotal.text = sumaTotal.toString()
            Log.d("PRODUCTO DE CARRRITO DE COMPRAS", "PRODUCTOS: $productos")

            // [productoJSON, productoJSON, productoJSON]
            buttonConfirmarPedido.isEnabled = true
        } else {
            productos = arrayListOf<CarritoProducto>()
            // []
            Log.d("PRODUCTO DE CARRRITO DE COMPRAS VACIO", "PRODUCTOS: $productos")
            Log.d("PRODUCTO DE CARRRITO DE COMPRAS VACIO", "PRODUCTOS: VACIO")

            buttonConfirmarPedido.isEnabled = false
        }
    }

    fun calcularTotal(productos: List<CarritoProducto>): Double {
        return productos.sumByDouble {
            (it.precioDescuento ?: 0.0) * (it.cantidad ?: 0)
        }
    }

    object CarritoToPedidoMapper {
        fun map(carritoProductos: ArrayList<CarritoProducto>): ArrayList<PedidoProductos> {
            return ArrayList(carritoProductos.map { carritoProducto ->
                PedidoProductos(
                    cantidad = carritoProducto.cantidad,
                    imagen = carritoProducto.imagen,
                    nombre = carritoProducto.nombre,
                    precio = carritoProducto.precioDescuento
                )
            })
        }
    }

    fun getCurrentDateAsStringLegacy(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(Calendar.getInstance().time)
    }

}