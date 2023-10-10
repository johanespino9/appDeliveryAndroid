package com.icontinental.appdelivery

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton

class LoginActivity : AppCompatActivity() {

    lateinit var buttonInciarSesion: AppCompatButton

    lateinit var buttonRegistrarUsuario: AppCompatButton

    lateinit var editTextCorreo: EditText
    lateinit var editTextContrasena: EditText

//    private lateinit var auth: FirebaseAuth

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("USER_PREFERENCES", Context.MODE_PRIVATE)

        buttonInciarSesion = findViewById(R.id.buttonIniciarSesion)

        buttonRegistrarUsuario = findViewById(R.id.buttonRegistrarUsuario)

        editTextCorreo = findViewById(R.id.editTextCorreoElectronico)

        editTextContrasena = findViewById(R.id.editTextContrasena)

//        auth = Firebase.auth

        buttonInciarSesion.setOnClickListener {

            val correoElectronico = editTextCorreo.text.toString()
            val contrasena = editTextContrasena.text.toString()

            if (!correoElectronico.isEmpty() && !contrasena.isEmpty())  {
//                auth.signInWithEmailAndPassword(correoElectronico, contrasena)
//                    .addOnCompleteListener(this) { task ->
//                        if (task.isSuccessful) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("INICIO SESION EXITOSO", "signInWithEmail:success")
//                            sharedPreferences.edit()
//                                .putString("email", correoElectronico)
//                                .apply()
//
//                            val intent = Intent(this, MainActivity::class.java)
//
//                            startActivity(intent)
//                        } else {
//                            Log.w("ERROR INICIO SESION", "signInWithEmail:failure", task.exception)
//                            Toast.makeText(this, "Usuario o clave incorrecta", Toast.LENGTH_SHORT).show()
//                        }
//                    }
            }
            else {
                Toast.makeText(this, "Usuario o clave están vacíos", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }


        }

        buttonRegistrarUsuario.setOnClickListener {
            val intent = Intent(this, RegisterUserActivity::class.java)

            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)        }
    }
}