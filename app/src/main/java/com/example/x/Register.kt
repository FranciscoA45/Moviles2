package com.example.x

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.x.databinding.ActivityRegisterBinding
import org.json.JSONObject
import java.util.regex.Pattern

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Vincula las vistas utilizando binding
        binding.btnRegistrar.setOnClickListener { validarDatos() }
    }

    private fun mainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun signInClicked(view: View) {
        startActivity(Intent(this, Login::class.java))
    }

    private fun validarDatos() {
        val nombre = binding.nameET.text.toString()
        val correo = binding.emailET.text.toString()
        val contraseña = binding.passwordET.text.toString()
        val confirmacionContraseña = binding.cPasswordET.text.toString()
        val patternName = Pattern.compile("^[a-zA-Z ]+\$")
        val patternEmail = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")

        var isValid = true

        if (nombre.isBlank()) {
            binding.nameET.error = "Por favor, ingresa tu nombre"
            isValid = false
        } else if (!patternName.matcher(nombre).matches()) {
            binding.nameET.error = "Por favor, ingresa un nombre válido (solo letras)"
            isValid = false
        } else {
            binding.nameET.error = null
        }

        if (correo.isBlank()) {
            binding.emailET.error = "Por favor, ingresa tu correo electrónico"
            isValid = false
        } else if (!patternEmail.matcher(correo).matches()) {
            binding.emailET.error = "Por favor, ingresa un correo electrónico válido"
            isValid = false
        } else {
            binding.emailET.error = null
        }

        if (contraseña.isBlank()) {
            binding.passwordET.error = "Por favor, ingresa tu contraseña"
            isValid = false
        } else if (!(contraseña.length in 8..16)) {
            binding.passwordET.error = "La contraseña debe tener entre 8 y 16 caracteres"
            isValid = false
        } else {
            binding.passwordET.error = null
        }

        if (confirmacionContraseña.isBlank()) {
            binding.cPasswordET.error = "Por favor, confirma tu contraseña"
            isValid = false
        } else if (contraseña != confirmacionContraseña) {
            binding.cPasswordET.error = "La contraseña y la confirmación de contraseña no coinciden"
            isValid = false
        } else {
            binding.cPasswordET.error = null
        }

        if (isValid) {
            registrarse()
        }
    }

    fun togglePasswordVisibility(view: View) {
        toggleVisibility(binding.passwordET, binding.passwordIcon)
    }

    fun toggleConfirmPasswordVisibility(view: View) {
        toggleVisibility(binding.cPasswordET, binding.cPasswordIcon)
    }

    private fun toggleVisibility(editText: EditText, imageView: ImageView) {
        if (editText.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            imageView.setImageResource(R.drawable.visible_password)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            imageView.setImageResource(R.drawable.hide_password)
        }
        editText.setSelection(editText.text.length)
    }

    private fun registrarse() {
        val name = binding.nameET.text.toString().trim()
        val email = binding.emailET.text.toString().trim()
        val password = binding.passwordET.text.toString().trim()

        val url = "http://192.168.183.117:8000/api/users"
        val body = JSONObject().apply {
            put("name", name)
            put("email", email)
            put("password", password)
        }

        val request = JsonObjectRequest(
            Request.Method.POST, url, body,
            { response ->
                Toast.makeText(this, "Felicidades, usuario agregado exitosamente", Toast.LENGTH_LONG).show()
                mainActivity()
            },
            { error ->
                if (error.networkResponse?.statusCode == 409) {
                    Toast.makeText(this, "Error: El usuario ya existe.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                }
                Log.e("MiTag", "No funcionó, $error")
            }
        )

        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}
