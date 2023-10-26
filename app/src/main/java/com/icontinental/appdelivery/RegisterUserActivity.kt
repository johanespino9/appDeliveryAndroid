package com.icontinental.appdelivery

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.android.gms.maps.model.LatLng

class RegisterUserActivity : AppCompatActivity() {

    lateinit var editTextNombres: EditText
    lateinit var editTextApellidos: EditText
    lateinit var editTextDireccion: EditText
    lateinit var editTextCorreoElectronico: EditText
    lateinit var editTextContrasena: EditText

    lateinit var buttonRegistrar: AppCompatButton

    lateinit var db: FirebaseFirestore

    private lateinit var auth: FirebaseAuth

    lateinit var autocompleteFragment: AutocompleteSupportFragment

//    lateinit var latLngUsuario: LatLng = LatLng(40.730610, -73.935242)
    var latLngUsuario: LatLng = LatLng(40.730610, -73.935242)
    var direccionUsuario: String = ""

    var nombres = ""
    var  apellidos = ""
    var email = ""
    var contrasena = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        Places.initialize(applicationContext, "AIzaSyBBjz_DGivwZ4_UYJL_hmInCbyPg4KSkko")

        db = Firebase.firestore

        auth = Firebase.auth

//        autocompleteFragment =
//            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
//                    as AutocompleteSupportFragment

//        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS, Place.Field.LAT_LNG))

        editTextNombres = findViewById(R.id.editTextNombres)
        editTextApellidos = findViewById(R.id.editTextApellidos)
        editTextCorreoElectronico = findViewById(R.id.editTextCorreoElectronico)
        editTextContrasena = findViewById(R.id.editTextContrasena)
//
        buttonRegistrar = findViewById(R.id.buttonRegistro)

//        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
//            override fun onPlaceSelected(place: Place) {
//                // TODO: Get info about the selected place.
//                Log.i("AUTOCOMPLETE", "Place: ${place.name}, ${place.address} ${place.addressComponents} ${place.latLng} ")
//
//                latLngUsuario = place.latLng
//                direccionUsuario = place.address
//            }
//
//            override fun onError(status: Status) {
//                // TODO: Handle the error.
//                Log.i("AUTOCOMPLETE", "An error occurred: $status")
//            }
//        })

        buttonRegistrar.setOnClickListener {

             nombres = editTextNombres.text.toString()
            apellidos = editTextApellidos.text.toString()
            email = editTextCorreoElectronico.text.toString()
            contrasena = editTextContrasena.text.toString()

            val arrayParametros = arrayListOf<String>(nombres, apellidos, /*direccionUsuario,*/ email, contrasena)

            val camposVacios = arrayParametros.filter { it.isEmpty() }

            val noTieneCamposVacios = camposVacios.isEmpty()

            Log.d("AAAAAAAAA", "$arrayParametros")
            Log.d("AAAAAAAAA", "$camposVacios")
            Log.d("AAAAAAAAA", "$noTieneCamposVacios")

            if (noTieneCamposVacios) {


                registrarUsuarioAuth(
                    email,
                    contrasena
                )
            } else {
                Toast.makeText(
                    baseContext,
                    "Uno de los campos estan vacíos",
                    Toast.LENGTH_SHORT,
                ).show()
            }


        }
    }

    fun registrarUsuarioAuth(email: String, contrasena: String) {
        auth.createUserWithEmailAndPassword(email, contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    Toast.makeText(
                        baseContext,
                        "Authentication successfully.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    val user = auth.currentUser
                    registrarUsuario(
                        nombres,
                        apellidos,
                        direccionUsuario,
                        latLngUsuario,
                        email
                    )
                    val intent = Intent(this, HomeActivity::class.java)

                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
//                    updateUI(null)
                }
            }
    }

    fun registrarUsuario(
        nombres: String,
        apellidos: String,
        direccion: String,
        latLngUsuario: LatLng,
        email: String
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val usuario = hashMapOf(
            "nombres" to nombres,
            "apellidos" to apellidos,
            "direccion" to direccion,
            "latitud" to latLngUsuario.latitude.toString(),
            "longitud" to latLngUsuario.longitude.toString(),
            "email" to email
        )

        if (uid != null) {
            db.collection("usuarios")
                .document(uid).set(usuario)
                //.add(usuario)
                .addOnSuccessListener { documentReference ->
                    Log.d("REGISTRO USUARIO", "DocumentSnapshot added with ID: ${uid}")
                    Toast.makeText(this, "Se registro nuevo usuario", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "DocumentSnapshot added with ID: ${uid}", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.w("ERROR EN REGISTRO DE USUARIO", "Error adding document", e)
                    Toast.makeText(this, "Hubo un error al registrar usuario", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "Error adding document: ${e}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Hubo un error al registrar usuario", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "Error adding document", Toast.LENGTH_SHORT).show()
        }
    }
}