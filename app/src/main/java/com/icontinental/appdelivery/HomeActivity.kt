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
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icontinental.appdelivery.model.Producto
import com.icontinental.fooddeliveryapp.componentes.productos.AdapterProductos

interface OnItemClickListener {
    fun onItemClick(position: Int)
}

class HomeActivity : AppCompatActivity() {

    lateinit var recyclerViewCategorias: RecyclerView

    lateinit var recyclerViewProductos: RecyclerView

    lateinit var buttonCerrarSesion: AppCompatButton

    lateinit var buttonCarrito: AppCompatButton

    lateinit var buttonPedidos: AppCompatButton

    lateinit var linearlayoutUbicacion: LinearLayoutCompat

    lateinit var textViewSaludo: TextView

//    lateinit var usuario: Usuario

    lateinit var db: FirebaseFirestore

    private lateinit var auth: FirebaseAuth

    lateinit var sharedPreference: SharedPreferences

    var productos: ArrayList<Producto> = arrayListOf()

//    var categorias: ArrayList<Categoria> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var contextPrueba = this

        sharedPreference = getSharedPreferences("USER_DATA_PREFERENCES", Context.MODE_PRIVATE)

        db = Firebase.firestore

        auth = Firebase.auth

//        obtenerUsuario()

        obtenerProductos()

//        obtenerCategorias()

//        recyclerViewCategorias = findViewById(R.id.recyclerviewCategorias)

        recyclerViewProductos = findViewById(R.id.recyclerviewMasPopulares)

        textViewSaludo = findViewById(R.id.textViewSaludo)

//        buttonAgregarNuevoProducto = findViewById(R.id.buttonAgregarNuevoProducto)

        buttonCerrarSesion = findViewById(R.id.buttonCerrarSesion)

        buttonCarrito = findViewById(R.id.buttonCarrito)

        buttonPedidos = findViewById(R.id.buttonPedidos)

//        linearlayoutUbicacion = findViewById(R.id.linearlayoutUbicacion)

//        buttonAgregarNuevoProducto.setOnClickListener {
//            agregarNuevoProducto(textViewSaludo.text.toString(), "Pizzas", 20.0, 15.0, "ic_pizza_americana")
//        }

        buttonCerrarSesion.setOnClickListener {
            auth.signOut()
            finish()
        }

        buttonCarrito.setOnClickListener {
            val intent = Intent(this, CarritoActivity::class.java)

            startActivity(intent)
        }

        buttonPedidos.setOnClickListener {
            val intent = Intent(this, PedidosActivity::class.java)

            startActivity(intent)
        }

//        linearlayoutUbicacion.setOnClickListener {
//
//            val intent = Intent(this, UbicacionActivity::class.java)
//            Log.d("LATITUD MAIN ACTIVITY", "latitud: ${usuario?.latitud}")
//            Log.d("LONGITUD MAIN ACTIVITY", "longitud: ${usuario?.longitud}")
//            intent.putExtra("latitud", usuario?.latitud ?: "0.0")
//            intent.putExtra("longitud", usuario?.longitud ?: "0.0")
//            intent.putExtra("nombres", usuario?.nombres ?: "")
//            intent.putExtra("apellidos", usuario?.apellidos ?: "")
//            intent.putExtra("direccion", usuario?.direccion ?: "")
//
//            startActivity(intent)
//        }

        resources.getIdentifier("ic_pizza", "drawable", packageName)

//        val adapter = AdapterCategorias(categorias, this, object : OnItemClickListener {
//            override fun onItemClick(position: Int) {
//                val categoria = categorias[position]
//                Log.d("CATEGORIA SELECCIONADA", "CATEGORIA nombre${categorias[position].nombre}")
//
//                val intent = Intent(contextPrueba, DetalleProducto::class.java)
//
//                intent.putExtra("nombre", categoria.nombre ?: "")
//                intent.putExtra("imagen", categoria.imagen ?: "")
//
//                startActivity(intent)
//            }
//        })

        val adapterProductos = AdapterProductos(productos, this, object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val producto = productos[position]
                Log.d("PRODUCTO SELECCIONADO", "producto nombre${productos[position].nombre}")

                val intent = Intent(contextPrueba, DetalleProducto::class.java)

                intent.putExtra("nombre", producto.nombre ?: "")
                intent.putExtra("categoria", producto.categoria ?: "")
                intent.putExtra("imagen", producto.imagen ?: "")
                intent.putExtra("precioDescuento", producto.precioDescuento ?: 0.0)
                intent.putExtra("precioRegular", producto.precioRegular ?: 0.0)

                startActivity(intent)
            }
        })

//        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManagerProductos = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

//        recyclerViewCategorias.layoutManager = linearLayoutManager
//        recyclerViewCategorias.adapter = adapter

        recyclerViewProductos.layoutManager = linearLayoutManagerProductos
        recyclerViewProductos.adapter = adapterProductos

        obtenerDatosUsuario()
    }

//    fun obtenerUsuario() {
//
//        val email = sharedPreference.getString("email", "")
//
//        Log.d("EMAIL PREFERENCES", "Email: $email")
//
//        db.collection("usuarios")
//            .whereEqualTo("email", email)
//            .get()
//            .addOnSuccessListener { documents ->
//                usuario = documents.first().toObject(Usuario::class.java)
//                Log.d("OBTENER USUARIO", "${usuario?.nombres} => ${usuario?.latitud} ${usuario?.longitud}")
//                guardarUsuarioEnMemoria(usuario)
//                val nombresValue = sharedPreference.getString("nombres", "")
//                textViewSaludo.text = "Hola, $nombresValue"
//            }
//            .addOnFailureListener { exception ->
//                Log.e("OBTENER USUARIO", "Error getting documents: ", exception)
//            }
//    }
//
//    fun guardarUsuarioEnMemoria(usuario: Usuario?) {
//        var editor = sharedPreference.edit()
//        Log.d("USUARIO LATITUD", usuario?.latitud ?: "1.1")
//        Log.d("USUARIO LONGITUD", usuario?.longitud.toString())
//        editor.putString("latitud",usuario?.latitud ?: "1.1")
//        editor.putString("longitud",usuario?.longitud ?: "1.1")
//        editor.putString("nombres",usuario?.nombres ?: "")
//        editor.putString("apellidos",usuario?.apellidos ?: "")
//        editor.putString("direccion",usuario?.direccion ?: "")
//        editor.apply()
//    }

    fun obtenerDatosUsuario() {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid
        val docRef = db.collection("usuarios").document(uid)
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d("DOCUMENTO", "DocumentSnapshot data: ${document.data}")
                val nombre = document.getString("nombres")
                val direccion = document.getString("direccion")
                val apellido = document.getString("apellidos")

                val editor = sharedPreference.edit()
                editor.putString("nombre", nombre)
                editor.putString("direccion", direccion)
                editor.putString("apellido", apellido)

                editor.apply()

            } else {
                Log.d("NO DOCUMENTO", "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d("ERROR", "get failed with ", exception)
        }
    }

    fun obtenerProductos() {

        db.collection("productos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    Log.d("OBTENER DOCUMENTO", "${document.id} => ${document.data}")

                    productos.add(document.toObject(Producto::class.java))
                }
                recyclerViewProductos.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("ERROR EN LEER DATOS", "Error getting documents.", exception)
            }
    }

//    fun obtenerCategorias() {
//
//        db.collection("categorias")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//
//                    Log.d("OBTENER DOCUMENTO", "${document.id} => ${document.data}")
//
//                    categorias.add(document.toObject(Categoria::class.java))
//                }
//                recyclerViewCategorias.adapter?.notifyDataSetChanged()
//            }
//            .addOnFailureListener { exception ->
//                Log.w("ERROR EN LEER DATOS", "Error getting documents.", exception)
//            }
//    }

    fun agregarNuevoProducto(
        nombre: String,
        categoria: String,
        precioRegular: Double,
        precioDescuento: Double,
        imagen: String
    ) {

        val producto = hashMapOf(
            "nombre" to nombre,
            "categoria" to categoria,
            "precioRegular" to precioRegular,
            "precioDescuento" to precioDescuento,
            "imagen" to imagen
        )

        db.collection("productos")
            .add(producto)
            .addOnSuccessListener { documentReference ->
                Log.d("CREACION DOCUMENTO", "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(this, "Se agrego nuevo producto", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "DocumentSnapshot added with ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("ERROR EN CREACION", "Error adding document", e)
                Toast.makeText(this, "Hubo un error al crear producto", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Error adding document: ${e}", Toast.LENGTH_SHORT).show()
            }
    }
}